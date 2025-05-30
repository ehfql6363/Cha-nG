package com.ssafy.chaing.blockchain.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ssafy.chaing.blockchain.config.Web3jConnectionManager;
import com.ssafy.chaing.blockchain.handler.contract.ContractHandler;
import com.ssafy.chaing.blockchain.handler.contract.input.ContractInput;
import com.ssafy.chaing.blockchain.handler.contract.input.LiveAccountInput;
import com.ssafy.chaing.blockchain.handler.contract.input.PaymentInfoInput;
import com.ssafy.chaing.blockchain.handler.contract.output.ContractOutput;
import com.ssafy.chaing.blockchain.handler.contract.output.ContractOverviewOutput;
import com.ssafy.chaing.blockchain.handler.contract.output.ContractRentOutput;
import com.ssafy.chaing.blockchain.web3j.ContractManager;
import com.ssafy.chaing.blockchain.web3j.ContractManager.PaymentInfo;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple13;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple6;
// TransactionManager는 이제 직접 주입받지 않음

@ExtendWith(MockitoExtension.class) // JUnit5 와 Mockito 연동
class ContractHandlerTest {

    private final String TEST_CONTRACT_ADDRESS = "0x7928F8BEa5E1d502eb5B882b7cab0d3e11a85e35";
    private final long TEST_CHAIN_ID = 137L; // 테스트용 체인 ID

    @Mock
    private Web3jConnectionManager mockConnectionManager; // 수정: ConnectionManager Mock
    @Mock
    private Credentials mockCredentials; // 수정: Credentials Mock
    @Mock
    private ContractManager mockContractManager; // 수정: ContractManager는 여전히 Mock 필요
    @Mock
    private Web3j mockWeb3j; // 수정: execute 콜백에 전달될 Web3j Mock

    // @InjectMocks 사용 시 Mockito가 생성자에 Mock 객체들을 주입 시도
    // 단, 생성자 주입 외 @Value 등이 있으면 직접 생성해야 할 수 있음
    private ContractHandler contractHandler;

    // --- Helper for mocking connectionManager.execute ---
    // 이 Answer는 connectionManager.execute가 호출될 때
    // 1. ContractManager.load가 mockContractManager를 반환하도록 설정하고 (정적 메서드 모킹 필요)
    // 2. 전달된 람다(callable)를 실행하는 것을 시뮬레이션합니다.
    // 정적 메서드 모킹 대신, 람다가 반환해야 할 최종 결과만 설정하는 것이 더 간단합니다.
    private <T> Answer<T> simulateExecution(T expectedResult) {
        return invocation -> {
            // 1. invocation에서 람다(Web3jCallable) 가져오기 (선택적)
            // Web3jConnectionManager.Web3jCallable<T> callable = invocation.getArgument(0);

            // 2. 람다가 실행될 때 반환될 결과 (가장 중요)
            //    실제 람다 실행 대신, 람다 실행의 *결과*를 반환하도록 설정합니다.
            //    이 결과는 보통 ContractManager의 메서드 호출(.send()) 결과입니다.
            //    따라서 각 테스트 메서드에서 mockContractManager의 동작을 미리 설정해두어야 합니다.
            return expectedResult;
        };
    }

    // execute가 예외를 던지도록 시뮬레이션하는 Answer
    private <T> Answer<T> simulateExecutionWithError(Exception exceptionToThrow) {
        return invocation -> {
            throw exceptionToThrow;
        };
    }


    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this) 대신 @ExtendWith(MockitoExtension.class) 사용
        // @InjectMocks를 사용하지 않고 수동으로 생성자 호출
        contractHandler = new ContractHandler(
                mockConnectionManager,
                mockCredentials,
                TEST_CHAIN_ID,
                TEST_CONTRACT_ADDRESS
        );
        // CustomGasProvider는 내부적으로 new로 생성되므로 별도 주입 불필요
        // ContractManager는 loadContractManager 헬퍼 내에서 로드되므로 필드 주입 불필요
    }

    @Test
    void testAddContract_Success() throws Exception { // CompletableFuture 예외 처리를 위해 throws Exception 추가
        // --- Input Data ---
        PaymentInfoInput paymentInfo1 = new PaymentInfoInput(BigInteger.valueOf(1), BigInteger.valueOf(2100000),
                BigInteger.valueOf(7));
        // ... (다른 PaymentInfoInput)
        ContractInput input = new ContractInput(/* ... input data 설정 ... */);
        input.setId(BigInteger.ONE);
        input.setPaymentInfos(List.of(paymentInfo1)); // 예시

        // --- Mocking ---
        // 1. 최종 결과인 TransactionReceipt Mock 설정
        TransactionReceipt mockReceipt = mock(TransactionReceipt.class);
        when(mockReceipt.isStatusOK()).thenReturn(true);

        // 2. connectionManager.execute가 호출되면 mockReceipt를 반환하도록 설정
        //    any()를 사용하여 어떤 Web3jCallable이든 동일하게 동작하도록 설정
        when(mockConnectionManager.execute(any(Web3jConnectionManager.Web3jCallable.class)))
                .thenAnswer(simulateExecution(mockReceipt)); // 성공 시 Receipt 반환

        // --- Execution ---
        CompletableFuture<Boolean> futureResult = contractHandler.addContract(input);

        // --- Verification ---
        assertTrue(futureResult.join(), "Contract should be added successfully");

        // connectionManager.execute가 정확히 1번 호출되었는지 검증 (선택적)
        verify(mockConnectionManager, times(1)).execute(any(Web3jConnectionManager.Web3jCallable.class));
    }

    @Test
    void testAddContract_Failure_ExceptionDuringExecution() throws Exception {
        // --- Input Data ---
        ContractInput input = new ContractInput(/* ... input data 설정 ... */);
        input.setId(BigInteger.TWO);

        // --- Mocking ---
        // connectionManager.execute가 호출될 때 RuntimeException을 던지도록 설정
        RuntimeException simulatedException = new RuntimeException("Blockchain connection failed");
        when(mockConnectionManager.execute(any(Web3jConnectionManager.Web3jCallable.class)))
                .thenAnswer(simulateExecutionWithError(simulatedException));

        // --- Execution ---
        CompletableFuture<Boolean> futureResult = contractHandler.addContract(input);

        // --- Verification ---
        assertFalse(futureResult.join(), "Should return false when an exception occurs");

        // connectionManager.execute가 호출되었는지 검증
        verify(mockConnectionManager, times(1)).execute(any(Web3jConnectionManager.Web3jCallable.class));
    }

    @Test
    void testGetContract() throws Exception {
        // --- Mocking Data ---
        BigInteger contractId = BigInteger.ONE;
        Tuple13<BigInteger, String, String, BigInteger, BigInteger, String, String, BigInteger, List<PaymentInfo>, String, Boolean, BigInteger, BigInteger> dummyTuple =
                new Tuple13<>(
                        contractId, "2025-01-01Z", "2025-12-31Z", BigInteger.valueOf(3000000), BigInteger.valueOf(5),
                        "112233445566", "998877665544", BigInteger.TEN,
                        List.of(new PaymentInfo(new Uint256(1), new Uint256(2100000), new Uint256(7))),
                        "123456789012", true, BigInteger.valueOf(3), BigInteger.valueOf(123)
                );

        // --- Mocking ---
        // connectionManager.execute가 호출되면 최종 결과인 dummyTuple을 반환하도록 설정
        when(mockConnectionManager.execute(any(Web3jConnectionManager.Web3jCallable.class)))
                .thenAnswer(simulateExecution(dummyTuple));

        // --- Execution ---
        ContractOutput result = contractHandler.getContract(contractId);

        // --- Verification ---
        assertNotNull(result);
        assertEquals(contractId, result.getId());
        assertEquals("2025-01-01Z", result.getStartDate());
        // ... (다른 필드 검증)

        // connectionManager.execute가 1번 호출되었는지 검증
        verify(mockConnectionManager, times(1)).execute(any(Web3jConnectionManager.Web3jCallable.class));
        // 중요: contractManager 자체의 메서드 호출을 직접 검증하는 대신,
        // connectionManager.execute의 호출과 그 결과를 검증합니다.
    }

    @Test
    void testGetContractOverview() throws Exception {
        // --- Mocking Data ---
        BigInteger contractId = BigInteger.ONE;
        Tuple3<BigInteger, String, String> dummyTuple =
                new Tuple3<>(contractId, "2025-01-01", "2025-12-31");

        // --- Mocking ---
        when(mockConnectionManager.execute(any(Web3jConnectionManager.Web3jCallable.class)))
                .thenAnswer(simulateExecution(dummyTuple));

        // --- Execution ---
        ContractOverviewOutput result = contractHandler.getContractOverview(contractId);

        // --- Verification ---
        assertNotNull(result);
        assertEquals(contractId, result.getId());
        assertEquals("2025-01-01", result.getStartDate());

        verify(mockConnectionManager, times(1)).execute(any(Web3jConnectionManager.Web3jCallable.class));
    }

    // ... (getPaymentInfoCount, getRentData 등 다른 읽기 테스트도 유사하게 수정) ...
    // 예시: getRentData
    @Test
    void testGetRentData() throws Exception {
        // --- Mocking Data ---
        BigInteger contractId = BigInteger.ONE;
        Tuple6<BigInteger, BigInteger, String, String, BigInteger, BigInteger> dummyTuple =
                new Tuple6<>(
                        BigInteger.valueOf(3000000), BigInteger.valueOf(5), "112233445566",
                        "665544332211", BigInteger.TEN, BigInteger.valueOf(3)
                );

        // --- Mocking ---
        when(mockConnectionManager.execute(any(Web3jConnectionManager.Web3jCallable.class)))
                .thenAnswer(simulateExecution(dummyTuple));

        // --- Execution ---
        ContractRentOutput result = contractHandler.getRentData(contractId);

        // --- Verification ---
        assertNotNull(result);
        assertEquals("112233445566", result.getRentAccountNo());

        verify(mockConnectionManager, times(1)).execute(any(Web3jConnectionManager.Web3jCallable.class));
    }


    @Test
    void testAddLiveAccount_Success() throws Exception {
        // --- Input ---
        BigInteger contractId = BigInteger.ONE;
        LiveAccountInput liveAccountInput = new LiveAccountInput("validAccountNo");

        // --- Mocking ---
        TransactionReceipt mockReceipt = mock(TransactionReceipt.class);
        when(mockReceipt.isStatusOK()).thenReturn(true);

        when(mockConnectionManager.execute(any(Web3jConnectionManager.Web3jCallable.class)))
                .thenAnswer(simulateExecution(mockReceipt));

        // --- Execution ---
        boolean result = contractHandler.addLiveAccount(contractId, liveAccountInput);

        // --- Verification ---
        assertTrue(result, "Adding live account should succeed");
        verify(mockConnectionManager, times(1)).execute(any(Web3jConnectionManager.Web3jCallable.class));
    }

    @Test
    void testAddLiveAccount_Failure_ReceiptNotOk() throws Exception {
        // --- Input ---
        BigInteger contractId = BigInteger.ONE;
        LiveAccountInput liveAccountInput = new LiveAccountInput("validAccountNo");

        // --- Mocking ---
        TransactionReceipt mockReceipt = mock(TransactionReceipt.class);
        when(mockReceipt.isStatusOK()).thenReturn(false); // 트랜잭션 실패 시뮬레이션

        when(mockConnectionManager.execute(any(Web3jConnectionManager.Web3jCallable.class)))
                .thenAnswer(simulateExecution(mockReceipt));

        // --- Execution ---
        boolean result = contractHandler.addLiveAccount(contractId, liveAccountInput);

        // --- Verification ---
        assertFalse(result, "Should return false when transaction receipt is not OK");
        verify(mockConnectionManager, times(1)).execute(any(Web3jConnectionManager.Web3jCallable.class));
    }

}