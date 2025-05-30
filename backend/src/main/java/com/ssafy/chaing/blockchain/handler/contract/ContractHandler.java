package com.ssafy.chaing.blockchain.handler.contract;

import com.ssafy.chaing.blockchain.config.Web3jConnectionManager;
import com.ssafy.chaing.blockchain.handler.contract.input.ContractInput;
import com.ssafy.chaing.blockchain.handler.contract.input.LiveAccountInput;
import com.ssafy.chaing.blockchain.handler.contract.output.ContractOutput;
import com.ssafy.chaing.blockchain.handler.contract.output.ContractOverviewOutput;
import com.ssafy.chaing.blockchain.handler.contract.output.ContractRentOutput;
import com.ssafy.chaing.blockchain.handler.contract.output.ContractUtilityOutput;
import com.ssafy.chaing.blockchain.handler.contract.output.LiveAccountOutput;
import com.ssafy.chaing.blockchain.handler.contract.output.PaymentInfoCountOutput;
import com.ssafy.chaing.blockchain.handler.contract.output.PaymentInfoOutput;
// 제거: import com.ssafy.chaing.blockchain.provider.CustomGasProvider;
import com.ssafy.chaing.blockchain.web3j.ContractManager; // 수정됨: web3j 패키지명 확인 필요
import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.common.exception.ExceptionCode;

import java.io.IOException; // 추가
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors; // 추가

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder; // 추가
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction; // 추가
import org.web3j.crypto.TransactionEncoder; // 추가
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName; // 추가
import org.web3j.protocol.core.methods.response.EthGetTransactionCount; // 추가
import org.web3j.protocol.core.methods.response.EthSendTransaction; // 추가
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException; // 추가
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider; // 추가
import org.web3j.tx.response.PollingTransactionReceiptProcessor; // 추가
import org.web3j.tx.response.TransactionReceiptProcessor; // 추가
import org.web3j.utils.Convert; // 추가
import org.web3j.utils.Numeric; // 추가


@Slf4j
@Component
public class ContractHandler {
    private final Web3jConnectionManager connectionManager;
    private final Credentials credentials;
    private final long chainId;
    private final String contractAddress;
    // 제거: private final CustomGasProvider gasProvider;
    private final ConcurrentHashMap<String, Object> accountLocks = new ConcurrentHashMap<>();

    // --- 가스 및 폴링 관련 상수 추가 ---
    private static final BigInteger DEFAULT_GAS_LIMIT = BigInteger.valueOf(5_000_000L); // Contract 배포/수정은 더 필요할 수 있음
    private static final BigInteger DEFAULT_MAX_PRIORITY_FEE_GWEI = BigInteger.valueOf(2L);
    private static final int POLLING_ATTEMPTS = 20;
    private static final long POLLING_FREQUENCY = 3000;

    @Autowired
    public ContractHandler(Web3jConnectionManager connectionManager,
                           @Qualifier("contractCredentials") Credentials contractsCredentials,
                           long chainId,
                           @Value("${web3j.contract-address}") String contractAddress) {
        log.info("ContractHandler initialized for contract address: {}", contractAddress);
        this.connectionManager = connectionManager;
        this.credentials = contractsCredentials;
        this.chainId = chainId;
        this.contractAddress = contractAddress;
        // 제거: this.gasProvider = new CustomGasProvider();
    }

    // --- 읽기 전용 ContractManager 로더 추가 ---
    private ContractManager loadContractManagerForRead(Web3j web3j) {
        TransactionManager readOnlyManager = new RawTransactionManager(web3j, credentials, chainId);
        return ContractManager.load(contractAddress, web3j, readOnlyManager, new DefaultGasProvider());
    }

    @Async
    public CompletableFuture<Boolean> addContract(ContractInput input) {

        return CompletableFuture.supplyAsync(() -> {
            String accountAddress = credentials.getAddress();
            Object accountLock = accountLocks.computeIfAbsent(accountAddress, k -> new Object());

            TransactionReceipt receipt = null; // try 블록 밖으로 이동
            boolean success = false;

            log.info("🔒 [CONTRACT] 계정 [{}] 락 획득 시도...", accountAddress);

            try {
                synchronized (accountLock) {
                    log.info("🔑 [CONTRACT] 계정 [{}] 락 획득 성공! (이제 트랜잭션 보냅니다)", accountAddress);

                    // --- Web3j 실행 컨텍스트 시작 ---
                    receipt = connectionManager.execute(web3j -> {
                        try {
                            // 1. EIP-1559 가스비 계산
                            BigInteger baseFeePerGas = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
                                    .send().getBlock().getBaseFeePerGas();
                            if (baseFeePerGas == null) {
                                throw new RuntimeException("Base Fee per gas not available.");
                            }
                            log.info("💰 Current Base Fee: {} Gwei", Convert.fromWei(baseFeePerGas.toString(), Convert.Unit.GWEI));

                            BigInteger maxPriorityFeePerGas;
                            try {
                                maxPriorityFeePerGas = web3j.ethMaxPriorityFeePerGas().send().getMaxPriorityFeePerGas();
                            } catch (IOException e) {
                                maxPriorityFeePerGas = Convert.toWei(DEFAULT_MAX_PRIORITY_FEE_GWEI.toString(), Convert.Unit.GWEI).toBigInteger();
                                log.warn("⚠️ eth_maxPriorityFeePerGas failed, using default: {} Gwei", DEFAULT_MAX_PRIORITY_FEE_GWEI);
                            }
                            log.info("💰 Max Priority Fee (Tip): {} Gwei", Convert.fromWei(maxPriorityFeePerGas.toString(), Convert.Unit.GWEI));

                            BigInteger maxFeePerGas = baseFeePerGas.multiply(BigInteger.valueOf(2)).add(maxPriorityFeePerGas);
                            log.info("💰 Calculated Max Fee: {} Gwei", Convert.fromWei(maxFeePerGas.toString(), Convert.Unit.GWEI));

                            // 2. Nonce 조회 (PENDING)
                            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                                    accountAddress, DefaultBlockParameterName.PENDING).send();
                            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
                            log.info("🔄 Nonce for account {}: {}", accountAddress, nonce);

                            // 3. 함수 호출 데이터 인코딩
                            List<ContractManager.PaymentInfo> paymentInfos = input.getPaymentInfos().stream()
                                    .map(pi -> new ContractManager.PaymentInfo(
                                            pi.getUserId(),
                                            pi.getAmount(),
                                            pi.getRatio()
                                    ))
                                    .collect(Collectors.toList()); // toList() 사용 권장

                            ContractManager encoderManager = loadContractManagerForRead(web3j); // 인코딩용 로더 사용
                            String encodedFunction = encoderManager.addContract(
                                    input.getId(), input.getStartDate(), input.getEndDate(),
                                    input.getRentTotalAmount(), input.getRentDueDate(), input.getRentAccountNo(),
                                    input.getOwnerAccountNo(), input.getRentTotalRatio(), paymentInfos,
                                    input.getLiveAccountNo(), input.getIsUtilityEnabled(), input.getUtilitySplitRatio(),
                                    input.getCardId()
                            ).encodeFunctionCall();

                            // 4. 가스 한도 (기본값 사용)
                            BigInteger gasLimit = DEFAULT_GAS_LIMIT;

                            // 5. EIP-1559 Raw Transaction 생성
                            RawTransaction rawTransaction = RawTransaction.createTransaction(
                                    chainId, nonce, gasLimit, contractAddress, BigInteger.ZERO, encodedFunction,
                                    maxPriorityFeePerGas, maxFeePerGas);

                            // 6. 트랜잭션 서명
                            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
                            String hexValue = Numeric.toHexString(signedMessage);

                            // 7. 서명된 트랜잭션 전송
                            log.info("🚀 [CONTRACT] 서명된 트랜잭션 전송 시도...");
                            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

                            if (ethSendTransaction.hasError()) {
                                throw new RuntimeException("Raw Transaction 전송 실패: " + ethSendTransaction.getError().getMessage());
                            }
                            String txHash = ethSendTransaction.getTransactionHash();
                            log.info("✅ [CONTRACT] 트랜잭션 전송 성공! Tx Hash: {}", txHash);

                            // 8. 트랜잭션 영수증 기다리기
                            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(
                                    web3j, POLLING_FREQUENCY, POLLING_ATTEMPTS);
                            TransactionReceipt txReceipt = receiptProcessor.waitForTransactionReceipt(txHash);
                            log.info("🧾 트랜잭션 [{}] 영수증 수신 완료. Status: {}", txHash, txReceipt.getStatus());
                            return txReceipt;

                        } catch (IOException e) {
                            log.error("🚨 Web3j 통신 에러: {}", e.getMessage(), e);
                            throw new RuntimeException("Web3j 통신 에러: " + e.getMessage(), e);
                        } catch (TransactionException e) {
                            log.error("🚨 트랜잭션 영수증 처리 에러: {}", e.getMessage(), e);
                            throw new RuntimeException("트랜잭션 영수증 처리 에러: " + e.getMessage(), e);
                        } catch (Exception e) {
                            log.error("🚨 예측하지 못한 에러: {}", e.getMessage(), e);
                            throw new RuntimeException("예측하지 못한 에러: " + e.getMessage(), e);
                        }
                    }); // --- Web3j 실행 컨텍스트 끝 ---
                } // --- synchronized 블록 끝 ---

                log.info("🔓 [CONTRACT] 계정 [{}] 락 해제됨. (트랜잭션 결과 처리 시작)", accountAddress);

                // 영수증 상태 확인 및 반환
                success = receipt != null && receipt.isStatusOK();
                String resultEmoji = success ? "😄 성공" : "😥 실패";
                log.info("✅ [CONTRACT] 최종 트랜잭션 처리 결과 - 계정 {}: {} (Tx: {})",
                        accountAddress, resultEmoji, receipt != null ? receipt.getTransactionHash() : "N/A");
                return success; // 최종 결과 반환

            } catch (Exception e) { // execute 밖의 예외 처리
                log.error("🚨 [CONTRACT] addContract 처리 중 최종 에러 발생! 계정: {}, 이유: {}", accountAddress, e.getMessage(), e);
                return false; // 비동기 작업 실패 시 false 반환
            }
        });
    }

    // --- addLiveAccount 메소드 수정 ---
    public boolean addLiveAccount(BigInteger contractId, LiveAccountInput input) {
        String accountAddress = credentials.getAddress();
        Object accountLock = accountLocks.computeIfAbsent(accountAddress, k -> new Object());
        TransactionReceipt receipt = null;
        boolean success = false;

        log.info("🔒 [CONTRACT-LiveAcc] 계정 [{}] 락 획득 시도 (Contract ID: {})...", accountAddress, contractId);

        try {
            synchronized (accountLock) {
                log.info("🔑 [CONTRACT-LiveAcc] 계정 [{}] 락 획득 성공!", accountAddress);

                receipt = connectionManager.execute(web3j -> {
                    try {
                        // 1. 가스비 계산
                        BigInteger baseFeePerGas = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getBaseFeePerGas();
                        if (baseFeePerGas == null) throw new RuntimeException("Base Fee not available.");
                        BigInteger maxPriorityFeePerGas;
                        try { maxPriorityFeePerGas = web3j.ethMaxPriorityFeePerGas().send().getMaxPriorityFeePerGas(); }
                        catch (IOException e) { maxPriorityFeePerGas = Convert.toWei(DEFAULT_MAX_PRIORITY_FEE_GWEI.toString(), Convert.Unit.GWEI).toBigInteger(); }
                        BigInteger maxFeePerGas = baseFeePerGas.multiply(BigInteger.valueOf(2)).add(maxPriorityFeePerGas);
                        log.info("[CONTRACT-LiveAcc] Gas Fees: Base={}, Prio={}, Max={}", baseFeePerGas, maxPriorityFeePerGas, maxFeePerGas);

                        // 2. Nonce 조회
                        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(accountAddress, DefaultBlockParameterName.PENDING).send();
                        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
                        log.info("[CONTRACT-LiveAcc] Nonce: {}", nonce);

                        // 3. 함수 데이터 인코딩
                        ContractManager encoderManager = loadContractManagerForRead(web3j);
                        String encodedFunction = encoderManager.updateLiveAccountNo(contractId, input.getLiveAccountNo()).encodeFunctionCall();

                        // 4. 가스 한도
                        BigInteger gasLimit = BigInteger.valueOf(1_000_000L); // 상태 업데이트는 가스 덜 필요할 수 있음, 추정 필요

                        // 5. Raw Tx 생성
                        RawTransaction rawTransaction = RawTransaction.createTransaction(
                                chainId, nonce, gasLimit, contractAddress, BigInteger.ZERO, encodedFunction,
                                maxPriorityFeePerGas, maxFeePerGas);

                        // 6. 서명
                        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
                        String hexValue = Numeric.toHexString(signedMessage);

                        // 7. 전송
                        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
                        if (ethSendTransaction.hasError()) { throw new RuntimeException("Tx 전송 실패: " + ethSendTransaction.getError().getMessage()); }
                        String txHash = ethSendTransaction.getTransactionHash();
                        log.info("[CONTRACT-LiveAcc] Tx 전송 성공! Hash: {}", txHash);

                        // 8. 영수증 대기
                        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(web3j, POLLING_FREQUENCY, POLLING_ATTEMPTS);
                        TransactionReceipt txReceipt = receiptProcessor.waitForTransactionReceipt(txHash);
                        log.info("[CONTRACT-LiveAcc] 영수증 수신 완료. Status: {}", txReceipt.getStatus());
                        return txReceipt;

                    } catch (Exception e) {
                        log.error("🚨 [CONTRACT-LiveAcc] execute 내부 에러: {}", e.getMessage(), e);
                        throw new RuntimeException(e); // 예외를 다시 던져 execute가 처리하도록 함
                    }
                });
            } // synchronized 끝

            log.info("🔓 [CONTRACT-LiveAcc] 계정 [{}] 락 해제됨.", accountAddress);
            success = receipt != null && receipt.isStatusOK();
            log.info("✅ [CONTRACT-LiveAcc] 최종 처리 결과 for Contract ID {}: {} (Tx: {})", contractId, success, receipt != null ? receipt.getTransactionHash() : "N/A");
            return success;

        } catch (Exception e) {
            log.error("🚨 [CONTRACT-LiveAcc] 최종 에러 for Contract ID {}: {}❗", contractId, e.getMessage(), e);
            return false; // 실패 시 false 반환
        }
    }

    // --- 모든 읽기 메소드에서 loadContractManagerForRead 사용 ---
    public List<?> getAllContracts() {
        try {
            return connectionManager.execute(web3j -> {
                ContractManager localContractManager = loadContractManagerForRead(web3j); // 수정
                log.info("Executing getAllContracts on: {}", connectionManager.getCurrentRpcEndpoint());
                return localContractManager.getAllContracts().send();
            });
        } catch (Exception e) {
            log.error("❗getAllContracts error: {}❗", e.getMessage(), e);
            throw new BadRequestException(ExceptionCode.CONTRACT_TRANSACTION_RETRIEVE_FAILED);
        }
    }

    public ContractOutput getContract(BigInteger id) {
        try {
            var tuple = connectionManager.execute(web3j -> {
                ContractManager localContractManager = loadContractManagerForRead(web3j); // 수정
                log.info("Executing getFullContractData for ID {} on: {}", id,
                        connectionManager.getCurrentRpcEndpoint());
                return localContractManager.getFullContractData(id).send();
            });

            // Null check for tuple components before accessing stream might be needed if contract returns empty/invalid data
            List<PaymentInfoOutput> dtos = tuple.component9() != null ? tuple.component9().stream()
                    .map(paymentInfo -> new PaymentInfoOutput(
                            paymentInfo.userId,
                            paymentInfo.amount,
                            paymentInfo.ratio))
                    .collect(Collectors.toList()) : List.of(); // 빈 리스트 반환

            // Null check other components if necessary
            return new ContractOutput(
                    tuple.component1(), tuple.component2(), tuple.component3(),
                    tuple.component4(), tuple.component5(), tuple.component6(),
                    tuple.component7(), tuple.component8(), dtos,
                    tuple.component10(), tuple.component11(), tuple.component12(),
                    tuple.component13()
            );
        } catch (Exception e) {
            log.error("❗getContract error for ID {}: {}❗", id, e.getMessage(), e);
            throw new BadRequestException(ExceptionCode.CONTRACT_TRANSACTION_RETRIEVE_FAILED);
        }
    }

    public ContractOverviewOutput getContractOverview(BigInteger id) {
        try {
            var tuple = connectionManager.execute(web3j -> {
                ContractManager localContractManager = loadContractManagerForRead(web3j); // 수정
                log.info("Executing getContractOverview for ID {} on: {}", id,
                        connectionManager.getCurrentRpcEndpoint());
                return localContractManager.getContractOverview(id).send();
            });
            return new ContractOverviewOutput(tuple.component1(), tuple.component2(), tuple.component3());
        } catch (Exception e) {
            log.error("❗getContractOverview error for ID {}: {}❗", id, e.getMessage(), e);
            throw new BadRequestException(ExceptionCode.CONTRACT_TRANSACTION_RETRIEVE_FAILED);
        }
    }

    public PaymentInfoCountOutput getPaymentInfoCount(BigInteger id) {
        try {
            BigInteger count = connectionManager.execute(web3j -> {
                ContractManager localContractManager = loadContractManagerForRead(web3j); // 수정
                log.info("Executing getPaymentInfoCount for ID {} on: {}", id,
                        connectionManager.getCurrentRpcEndpoint());
                return localContractManager.getPaymentInfoCount(id).send();
            });
            return new PaymentInfoCountOutput(count);
        } catch (Exception e) {
            log.error("❗getPaymentInfoCount error for ID {}: {}❗", id, e.getMessage(), e); // 로그 메시지 수정
            throw new BadRequestException(ExceptionCode.CONTRACT_TRANSACTION_RETRIEVE_FAILED);
        }
    }

    public PaymentInfoOutput getPaymentInfoByIndex(BigInteger id, BigInteger index) {
        try {
            var tuple = connectionManager.execute(web3j -> {
                ContractManager localContractManager = loadContractManagerForRead(web3j); // 수정
                log.info("Executing getPaymentInfoByIndex for ID {}, Index {} on: {}", id, index, // 로그 개선
                        connectionManager.getCurrentRpcEndpoint());
                return localContractManager.getPaymentInfoByIndex(id, index).send();
            });
            return new PaymentInfoOutput(tuple.component1(), tuple.component2(), tuple.component3());
        } catch (Exception e) {
            log.error("❗getPaymentInfoByIndex error for ID {}, Index {}: {}❗", id, index, e.getMessage(), e); // 로그 개선
            throw new BadRequestException(ExceptionCode.CONTRACT_TRANSACTION_RETRIEVE_FAILED);
        }
    }

    public ContractRentOutput getRentData(BigInteger id) {
        try {
            var tuple = connectionManager.execute(web3j -> {
                ContractManager localContractManager = loadContractManagerForRead(web3j); // 수정
                log.info("Executing getRentData for ID {} on: {}", id,
                        connectionManager.getCurrentRpcEndpoint());
                return localContractManager.getRentData(id).send();
            });
            return new ContractRentOutput(
                    tuple.component1(), tuple.component2(), tuple.component3(),
                    tuple.component4(), tuple.component5(), tuple.component6()
            );
        } catch (Exception e) {
            log.error("❗getRentData error for ID {}: {}❗", id, e.getMessage(), e); // 에러 로깅 추가
            throw new BadRequestException(ExceptionCode.CONTRACT_TRANSACTION_RETRIEVE_FAILED);
        }
    }

    public ContractUtilityOutput getUtilityData(BigInteger id) {
        try {
            var tuple = connectionManager.execute(web3j -> {
                ContractManager contractManager = loadContractManagerForRead(web3j); // 수정
                log.info("Executing getUtilityData for ID {} on: {}", id,
                        connectionManager.getCurrentRpcEndpoint());
                return contractManager.getUtilityData(id).send();
            });
            return new ContractUtilityOutput(
                    tuple.component1(), tuple.component2(), tuple.component3()
            );
        } catch (Exception e) {
            log.error("❗getUtilityData error for ID {}: {}❗", id, e.getMessage(), e); // 에러 로깅 추가
            throw new BadRequestException(ExceptionCode.CONTRACT_TRANSACTION_RETRIEVE_FAILED);
        }
    }

    public LiveAccountOutput getLiveAccount(BigInteger contractId) {
        try {
            String liveAccountNo = connectionManager.execute(web3j -> {
                ContractManager localContractManager = loadContractManagerForRead(web3j); // 수정
                log.info("Executing getLiveAccountNo for Contract ID {} on: {}", contractId,
                        connectionManager.getCurrentRpcEndpoint());
                return localContractManager.getLiveAccountNo(contractId).send();
            });
            return new LiveAccountOutput(liveAccountNo);
        } catch (Exception e) {
            log.error("❗getLiveAccount error for Contract ID {}: {}❗", contractId, e.getMessage(), e);
            throw new BadRequestException(ExceptionCode.LIVE_ACCOUNT_RETRIEVE_FAILED);
        }
    }
}