package com.ssafy.chaing.blockchain.handler.rent;

import com.ssafy.chaing.blockchain.config.Web3jConnectionManager;
import com.ssafy.chaing.blockchain.handler.rent.input.RentInput;
import com.ssafy.chaing.blockchain.handler.rent.output.RentOutput;
// 제거: import com.ssafy.chaing.blockchain.provider.CustomGasProvider;
import com.ssafy.chaing.blockchain.web3j.RentManager;
import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.common.exception.ExceptionCode;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.RawTransactionManager; // 여전히 Nonce 관리에 필요할 수 있음
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider; // 읽기 전용 호출에 사용
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;


@Slf4j
@Component
public class RentHandler {

    private final Web3jConnectionManager connectionManager;
    private final Credentials credentials;
    private final long chainId;
    private final String rentAddress;
    // 제거: private final CustomGasProvider gasProvider;
    private final ConcurrentHashMap<String, Object> accountLocks = new ConcurrentHashMap<>();

    // 가스 관련 상수 (설정값으로 빼는 것이 더 좋을 수 있음)
    private static final BigInteger DEFAULT_GAS_LIMIT = BigInteger.valueOf(4_500_000L); // 기본 가스 한도
    private static final BigInteger DEFAULT_MAX_PRIORITY_FEE_GWEI = BigInteger.valueOf(2L); // 기본 팁 (Gwei)

    // 트랜잭션 영수증 폴링 설정
    private static final int POLLING_ATTEMPTS = 20; // 시도 횟수
    private static final long POLLING_FREQUENCY = 3000; // 폴링 간격 (ms)


    @Autowired
    public RentHandler(Web3jConnectionManager connectionManager,
                       @Qualifier("rentCredentials") Credentials rentCredentials,
                       long chainId,
                       @Value("${web3j.rent-contract-address}") String rentAddress) {
        this.connectionManager = connectionManager;
        this.credentials = rentCredentials;
        this.chainId = chainId;
        this.rentAddress = rentAddress;
        // 제거: this.gasProvider = new CustomGasProvider();
        log.info("✅ RentHandler 초기화 완료! 계약 주소: {}", rentAddress);
    }

    // 읽기 전용 호출을 위한 RentManager 로드 (가스비 불필요)
    private RentManager loadRentManagerForRead(Web3j web3j) {
        // 읽기 전용 호출에는 실제 트랜잭션 매니저나 가스 공급자가 중요하지 않을 수 있음
        // null 대신 DefaultGasProvider를 사용하거나, view/pure 함수 호출 방식에 따라 적절히 설정
        TransactionManager readOnlyManager = new RawTransactionManager(web3j, credentials, chainId); // Nonce 관리는 안함
        return RentManager.load(rentAddress, web3j, readOnlyManager, new DefaultGasProvider());
    }


    @Async
    public CompletableFuture<Boolean> addContract(RentInput input) {

        return CompletableFuture.supplyAsync(() -> {
            String accountAddress = credentials.getAddress();
            Object accountLock = accountLocks.computeIfAbsent(accountAddress, k -> new Object());

            TransactionReceipt receipt = null;
            boolean success = false;

            log.info("🔒 [RENT] 계정 [{}] 락 획득 시도...", accountAddress);

            try {
                synchronized (accountLock) {
                    log.info("🔑 [RENT] 계정 [{}] 락 획득 성공! (이제 트랜잭션 보냅니다)", accountAddress);

                    // Web3j 연결 및 트랜잭션 실행
                    receipt = connectionManager.execute(web3j -> {
                        try {
                            // --- 1. EIP-1559 가스비 계산 ---
                            BigInteger baseFeePerGas = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
                                    .send().getBlock().getBaseFeePerGas();
                            if (baseFeePerGas == null) {
                                throw new RuntimeException("현재 블록에서 Base Fee를 가져올 수 없습니다. 노드가 EIP-1559를 지원하는지 확인하세요.");
                            }
                            log.info("💰 Current Base Fee: {} Gwei", Convert.fromWei(baseFeePerGas.toString(), Convert.Unit.GWEI));

                            // 팁 (Priority Fee) 설정 (노드에서 조회하거나 기본값 사용)
                            BigInteger maxPriorityFeePerGas;
                            try {
                                maxPriorityFeePerGas = web3j.ethMaxPriorityFeePerGas().send().getMaxPriorityFeePerGas();
                                log.info("💰 Fetched Max Priority Fee: {} Gwei", Convert.fromWei(maxPriorityFeePerGas.toString(), Convert.Unit.GWEI));
                            } catch (IOException e) {
                                maxPriorityFeePerGas = Convert.toWei(DEFAULT_MAX_PRIORITY_FEE_GWEI.toString(), Convert.Unit.GWEI).toBigInteger();
                                log.warn("⚠️ eth_maxPriorityFeePerGas 호출 실패 또는 노드 미지원. 기본 팁 사용: {} Gwei", DEFAULT_MAX_PRIORITY_FEE_GWEI);
                            }

                            // 최대 가스비 (Max Fee) 계산 (Base Fee * 2 + Tip)
                            BigInteger maxFeePerGas = baseFeePerGas.multiply(BigInteger.valueOf(2)).add(maxPriorityFeePerGas);
                            log.info("💰 Calculated Max Fee: {} Gwei", Convert.fromWei(maxFeePerGas.toString(), Convert.Unit.GWEI));

                            // --- 2. Nonce 조회 ---
                            // PENDING 상태의 Nonce를 사용해야 동시에 여러 트랜잭션 요청 시 순서 보장됨
                            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                                            accountAddress, DefaultBlockParameterName.PENDING)
                                    .send();
                            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
                            log.info("🔄 Nonce for account {}: {}", accountAddress, nonce);

                            // --- 3. 함수 호출 데이터 인코딩 ---
                            // 임시 RentManager를 사용하여 함수 호출 데이터만 생성
                            RentManager encoderManager = RentManager.load(rentAddress, web3j, credentials, new DefaultGasProvider());
                            String encodedFunction = encoderManager.addTransaction(
                                    input.getId(), input.getContractId(), input.getMonth(), input.getFrom(),
                                    input.getTo(), input.getAmount(), input.getStatus(), input.getTime()
                            ).encodeFunctionCall();

                            // --- 4. 가스 한도 예측 (선택 사항, 더 정확하게 하려면) ---
                            // Transaction estimateGasTx = Transaction.createEthCallTransaction(accountAddress, rentAddress, encodedFunction);
                            // EthEstimateGas estimate = web3j.ethEstimateGas(estimateGasTx).send();
                            // BigInteger estimatedGasLimit = estimate.getAmountUsed().multiply(BigInteger.valueOf(12)).divide(BigInteger.TEN); // 20% 여유
                            // log.info("⛽ Estimated Gas Limit: {}", estimatedGasLimit);
                            // 사용할 가스 한도 결정 (예측값 또는 기본값)
                            BigInteger gasLimit = DEFAULT_GAS_LIMIT; // 예측 대신 기본값 사용

                            // --- 5. EIP-1559 Raw Transaction 생성 ---
                            RawTransaction rawTransaction = RawTransaction.createTransaction(
                                    chainId,
                                    nonce,
                                    gasLimit,
                                    rentAddress,        // 컨트랙트 주소
                                    BigInteger.ZERO,    // Ether 전송량 (컨트랙트 호출 시 0)
                                    encodedFunction,
                                    maxPriorityFeePerGas,
                                    maxFeePerGas
                            );

                            // --- 6. 트랜잭션 서명 ---
                            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
                            String hexValue = Numeric.toHexString(signedMessage);

                            // --- 7. 서명된 트랜잭션 전송 ---
                            log.info("🚀 [Rent] 서명된 트랜잭션 전송 시도... 계정: {}, 노드: {}", accountAddress, connectionManager.getCurrentRpcEndpoint());
                            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

                            if (ethSendTransaction.hasError()) {
                                log.error("🚨 Raw Transaction 전송 실패: {}", ethSendTransaction.getError().getMessage());
                                throw new RuntimeException("Raw Transaction 전송 실패: " + ethSendTransaction.getError().getMessage());
                            }

                            String txHash = ethSendTransaction.getTransactionHash();
                            log.info("✅ [Rent] 트랜잭션 전송 성공! Tx Hash: {}", txHash);

                            // --- 8. 트랜잭션 영수증 기다리기 ---
                            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(
                                    web3j, POLLING_FREQUENCY, POLLING_ATTEMPTS);

                            TransactionReceipt txReceipt = receiptProcessor.waitForTransactionReceipt(txHash);
                            log.info("🧾 트랜잭션 [{}] 영수증 수신 완료. Status: {}", txHash, txReceipt.getStatus());
                            return txReceipt; // 성공적으로 영수증 받으면 반환

                        } catch (IOException e) {
                            log.error("🚨 Web3j 통신 중 에러 발생: {}", e.getMessage(), e);
                            throw new RuntimeException("Web3j 통신 에러: " + e.getMessage(), e);
                        } catch (TransactionException e) {
                            log.error("🚨 트랜잭션 처리 중 에러 발생 (영수증 폴링 중): {}", e.getMessage(), e);
                            throw new RuntimeException("트랜잭션 영수증 처리 에러: " + e.getMessage(), e);
                        } catch (Exception e) {
                            log.error("🚨 예측하지 못한 에러 발생: {}", e.getMessage(), e);
                            throw new RuntimeException("예측하지 못한 에러: " + e.getMessage(), e);
                        }
                    }); // End of connectionManager.execute()
                } // End of synchronized block

                log.info("🔓 [RENT] 계정 [{}] 락 해제됨. (트랜잭션 결과 처리 시작)", accountAddress);

                // 영수증 상태 확인
                success = receipt != null && receipt.isStatusOK();
                String resultEmoji = success ? "😄 성공" : "😥 실패";
                log.info("✅ [Rent] 최종 트랜잭션 처리 결과 - 계정 {}: {} (Tx: {})",
                        accountAddress, resultEmoji, receipt != null ? receipt.getTransactionHash() : "N/A");

            } catch (Exception e) {
                // connectionManager.execute 에서 발생한 예외 포함
                log.error("🚨 [Rent] addContract 처리 중 최종 에러 발생! 계정: {}, 이유: {}", accountAddress, e.getMessage(), e);
                success = false;
            }

            return success;
        }); // End of supplyAsync
    }

    // --- 기존의 getAllTransactions, getTransactionsByAccountId 메소드는 유지 ---
    // 내부적으로 loadRentManagerForRead 를 사용하도록 수정하면 좋음

    public List<?> getAllTransactions() {
        try {
            return connectionManager.execute(web3j -> {
                RentManager localRentManager = loadRentManagerForRead(web3j); // 읽기용 로더 사용
                log.info("📜 [Rent] 모든 트랜잭션 조회 시작... 노드: {}", connectionManager.getCurrentRpcEndpoint());
                return localRentManager.getAllTransactions().send();
            });
        } catch (Exception e) {
            log.error("🚨 [Rent] 모든 트랜잭션 조회 실패! 이유: {}", e.getMessage(), e);
            throw new BadRequestException(ExceptionCode.TRANSFER_TRANSACTION_RETRIEVE_FAILED);
        }
    }

    public List<RentOutput> getTransactionsByAccountId(BigInteger accountId) {
        try {
            List<?> rawList = connectionManager.execute(web3j -> {
                RentManager localRentManager = loadRentManagerForRead(web3j); // 읽기용 로더 사용
                log.info("👤 [Rent] 계정 ID [{}] 트랜잭션 조회 시작... 노드: {}", accountId, connectionManager.getCurrentRpcEndpoint());
                return localRentManager.getTransactionsByAccount(accountId).send();
            });

            List<RentOutput> dtoList = new ArrayList<>();
            // ... (기존의 데이터 변환 로직) ...
            if (rawList != null) {
                for (Object obj : rawList) {
                    if (obj instanceof DynamicStruct) {
                        DynamicStruct struct = (DynamicStruct) obj;
                        try {
                            List<Object> values = struct.getNativeValueCopy();
                            RentOutput dto = new RentOutput(
                                    (BigInteger) values.get(0), (BigInteger) values.get(1),
                                    (BigInteger) values.get(2), (String) values.get(3),
                                    (String) values.get(4), (BigInteger) values.get(5),
                                    (Boolean) values.get(6), (String) values.get(7)
                            );
                            dtoList.add(dto);
                        } catch (Exception castingException){
                            log.error("❌ 데이터 변환 오류! DynamicStruct -> RentOutput 실패. 데이터: {}, 오류: {}", struct, castingException.getMessage());
                        }
                    } else {
                        log.warn("🤔 예상치 못한 데이터 타입 발견! 타입: {}", obj != null ? obj.getClass().getName() : "null");
                    }
                }
            }
            return dtoList;

        } catch (Exception e) {
            log.error("🚨 [Rent] 계정 ID [{}] 트랜잭션 조회 실패! 이유: {}", accountId, e.getMessage(), e);
            throw new BadRequestException(ExceptionCode.TRANSFER_TRANSACTION_RETRIEVE_FAILED);
        }
    }
}