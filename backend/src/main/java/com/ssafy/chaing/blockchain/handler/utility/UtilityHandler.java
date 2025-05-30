package com.ssafy.chaing.blockchain.handler.utility; // 패키지 경로는 맞게 수정하세요

import com.ssafy.chaing.blockchain.config.Web3jConnectionManager;
import com.ssafy.chaing.blockchain.handler.utility.input.UtilityInput;
import com.ssafy.chaing.blockchain.handler.utility.output.UtilityOutput;
// 제거: import com.ssafy.chaing.blockchain.provider.CustomGasProvider;
import com.ssafy.chaing.blockchain.web3j.UtilityManager; // 수정됨: web3j 패키지명 확인 필요
import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.common.exception.ExceptionCode;

import java.io.IOException; // 추가
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder; // 추가
import org.web3j.abi.datatypes.DynamicStruct; // 추가 (기존에 있었음)
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
public class UtilityHandler {
    private final Web3jConnectionManager connectionManager;
    private final Credentials credentials;
    private final long chainId;
    private final String utilityAddress;
    // 제거: private final CustomGasProvider gasProvider;
    private final ConcurrentHashMap<String, Object> accountLocks = new ConcurrentHashMap<>();

    // --- 가스 및 폴링 관련 상수 추가 ---
    private static final BigInteger DEFAULT_GAS_LIMIT = BigInteger.valueOf(4_500_000L); // Rent와 유사하게 설정
    private static final BigInteger DEFAULT_MAX_PRIORITY_FEE_GWEI = BigInteger.valueOf(2L);
    private static final int POLLING_ATTEMPTS = 20;
    private static final long POLLING_FREQUENCY = 3000;


    @Autowired
    public UtilityHandler(Web3jConnectionManager connectionManager,
                          @Qualifier("utilityCredentials") Credentials utilityCredentials,
                          long chainId,
                          @Value("${web3j.utility-contract-address}") String utilityAddress) {
        this.connectionManager = connectionManager;
        this.credentials = utilityCredentials;
        this.chainId = chainId;
        this.utilityAddress = utilityAddress;
        // 제거: this.gasProvider = new CustomGasProvider();
        log.info("✅ UtilityHandler 초기화 완료! 계약 주소: {}", utilityAddress);
    }

    // --- 읽기/인코딩용 UtilityManager 로더 추가 ---
    private UtilityManager loadUtilityManagerForRead(Web3j web3j) {
        TransactionManager readOnlyManager = new RawTransactionManager(web3j, credentials, chainId);
        return UtilityManager.load(utilityAddress, web3j, readOnlyManager, new DefaultGasProvider());
    }

    @Async
    public CompletableFuture<Boolean> addContract(UtilityInput input) { // 메소드 이름이 addContract 이지만 Utility의 addTransaction 호출
        return CompletableFuture.supplyAsync(() -> {
            String accountAddress = credentials.getAddress();
            Object accountLock = accountLocks.computeIfAbsent(accountAddress, k -> new Object());

            TransactionReceipt receipt = null;
            boolean success = false;

            log.info("🔒 [UTILITY] 계정 [{}] 락 획득 시도...", accountAddress);

            try {
                synchronized (accountLock) {
                    log.info("🔑 [UTILITY] 계정 [{}] 락 획득 성공! (이제 트랜잭션 보냅니다)", accountAddress);

                    receipt = connectionManager.execute(web3j -> {
                        try {
                            // 1. EIP-1559 가스비 계산
                            BigInteger baseFeePerGas = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getBaseFeePerGas();
                            if (baseFeePerGas == null) throw new RuntimeException("Base Fee not available.");
                            log.info("💰 Current Base Fee: {} Gwei", Convert.fromWei(baseFeePerGas.toString(), Convert.Unit.GWEI));

                            BigInteger maxPriorityFeePerGas;
                            try { maxPriorityFeePerGas = web3j.ethMaxPriorityFeePerGas().send().getMaxPriorityFeePerGas(); }
                            catch (IOException e) { maxPriorityFeePerGas = Convert.toWei(DEFAULT_MAX_PRIORITY_FEE_GWEI.toString(), Convert.Unit.GWEI).toBigInteger(); log.warn("⚠️ eth_maxPriorityFeePerGas failed, using default: {} Gwei", DEFAULT_MAX_PRIORITY_FEE_GWEI); }
                            log.info("💰 Max Priority Fee (Tip): {} Gwei", Convert.fromWei(maxPriorityFeePerGas.toString(), Convert.Unit.GWEI));

                            BigInteger maxFeePerGas = baseFeePerGas.multiply(BigInteger.valueOf(2)).add(maxPriorityFeePerGas);
                            log.info("💰 Calculated Max Fee: {} Gwei", Convert.fromWei(maxFeePerGas.toString(), Convert.Unit.GWEI));


                            // 2. Nonce 조회 (PENDING)
                            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(accountAddress, DefaultBlockParameterName.PENDING).send();
                            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
                            log.info("🔄 Nonce for account {}: {}", accountAddress, nonce);

                            // 3. 함수 호출 데이터 인코딩
                            UtilityManager encoderManager = loadUtilityManagerForRead(web3j);
                            String encodedFunction = encoderManager.addTransaction( // UtilityManager의 함수 호출
                                    input.getId(), input.getContractId(), input.getMonth(), input.getFrom(),
                                    input.getTo(), input.getAmount(), input.getStatus(), input.getTime()
                            ).encodeFunctionCall();

                            // 4. 가스 한도 (기본값 사용)
                            BigInteger gasLimit = DEFAULT_GAS_LIMIT;

                            // 5. EIP-1559 Raw Transaction 생성
                            RawTransaction rawTransaction = RawTransaction.createTransaction(
                                    chainId, nonce, gasLimit, utilityAddress, BigInteger.ZERO, encodedFunction, // utilityAddress 사용
                                    maxPriorityFeePerGas, maxFeePerGas);

                            // 6. 트랜잭션 서명
                            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
                            String hexValue = Numeric.toHexString(signedMessage);

                            // 7. 서명된 트랜잭션 전송
                            log.info("🚀 [UTILITY] 서명된 트랜잭션 전송 시도...");
                            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

                            if (ethSendTransaction.hasError()) {
                                throw new RuntimeException("Raw Transaction 전송 실패: " + ethSendTransaction.getError().getMessage());
                            }
                            String txHash = ethSendTransaction.getTransactionHash();
                            log.info("✅ [UTILITY] 트랜잭션 전송 성공! Tx Hash: {}", txHash);


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
                    }); // connectionManager.execute 끝
                } // synchronized 끝

                log.info("🔓 [UTILITY] 계정 [{}] 락 해제됨. (트랜잭션 결과 처리 시작)", accountAddress);

                success = receipt != null && receipt.isStatusOK();
                String resultEmoji = success ? "😄 성공" : "😥 실패";
                log.info("✅ [UTILITY] 최종 트랜잭션 처리 결과 - 계정 {}: {} (Tx: {})",
                        accountAddress, resultEmoji, receipt != null ? receipt.getTransactionHash() : "N/A");

            } catch (Exception e) {
                log.error("🚨 [UTILITY] addContract 처리 중 최종 에러 발생! 계정: {}, 이유: {}", accountAddress, e.getMessage(), e);
                success = false; // 예외 발생 시 실패 처리
            }

            return success;
        }); // CompletableFuture 끝
    }

    // --- 읽기 메소드 수정: loadUtilityManagerForRead 사용 ---
    public List<?> getAllTransactions() {
        try {
            return connectionManager.execute(web3j -> {
                UtilityManager localUtilityManager = loadUtilityManagerForRead(web3j); // 수정
                log.info("Executing getAllTransactions (Utility) on: {}", connectionManager.getCurrentRpcEndpoint());
                return localUtilityManager.getAllTransactions().send();
            });
        } catch (Exception e) {
            log.error("❗Error retrieving all utility transactions: {}❗", e.getMessage(), e);
            throw new BadRequestException(ExceptionCode.TRANSFER_TRANSACTION_RETRIEVE_FAILED);
        }
    }

    public List<UtilityOutput> getTransactionsByAccountId(BigInteger accountId) {
        try {
            List<?> rawList = connectionManager.execute(web3j -> {
                UtilityManager localUtilityManager = loadUtilityManagerForRead(web3j); // 수정
                log.info("Executing getTransactionsByAccount (Utility) for Account ID {} on: {}", accountId,
                        connectionManager.getCurrentRpcEndpoint());
                return localUtilityManager.getTransactionsByAccount(accountId).send();
            });

            List<UtilityOutput> dtoList = new ArrayList<>();
            // ... (기존 데이터 변환 로직 유지) ...
            if (rawList != null) {
                for (Object obj : rawList) {
                    if (obj instanceof DynamicStruct) {
                        DynamicStruct struct = (DynamicStruct) obj;
                        try {
                            List<Object> values = struct.getNativeValueCopy();
                            UtilityOutput dto = new UtilityOutput(
                                    (BigInteger) values.get(0), (BigInteger) values.get(1),
                                    (BigInteger) values.get(2), (String) values.get(3),
                                    (String) values.get(4), (BigInteger) values.get(5),
                                    (Boolean) values.get(6), (String) values.get(7)
                            );
                            dtoList.add(dto);
                        } catch (Exception castingException){
                            log.error("❌ 데이터 변환 오류! DynamicStruct -> UtilityOutput 실패. 데이터: {}, 오류: {}", struct, castingException.getMessage());
                        }
                    } else {
                        log.warn("🤔 예상치 못한 데이터 타입 발견! 타입: {}", obj != null ? obj.getClass().getName() : "null");
                    }
                }
            }
            return dtoList;

        } catch (Exception e) {
            log.error("❗Error retrieving utility transactions for Account ID {}: {}❗", accountId, e.getMessage(), e);
            throw new BadRequestException(ExceptionCode.TRANSFER_TRANSACTION_RETRIEVE_FAILED);
        }
    }
}