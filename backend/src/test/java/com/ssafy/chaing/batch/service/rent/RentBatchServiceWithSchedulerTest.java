package com.ssafy.chaing.batch.service.rent;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ssafy.chaing.batch.config.RentBatchConfig;
import com.ssafy.chaing.batch.config.BatchInitializer;
import com.ssafy.chaing.batch.config.ExecutionTime;
import com.ssafy.chaing.batch.service.RentBatchService;
import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.contract.domain.ContractUserEntity;
import com.ssafy.chaing.contract.repository.ContractRepository;
import com.ssafy.chaing.contract.service.ContractService;
import com.ssafy.chaing.contract.service.command.ApproveContractCommand;
import com.ssafy.chaing.contract.service.command.ConfirmContractCommand;
import com.ssafy.chaing.contract.service.dto.ContractDTO;
import com.ssafy.chaing.fintech.service.FintechService;
import com.ssafy.chaing.fintech.service.dto.TransferDTO;
import com.ssafy.chaing.group.service.GroupService;
import com.ssafy.chaing.group.service.command.CreateGroupCommand;
import com.ssafy.chaing.group.service.command.JoinGroupCommand;
import com.ssafy.chaing.group.service.dto.GroupDTO;
import com.ssafy.chaing.payment.domain.FeeType;
import com.ssafy.chaing.payment.domain.PaymentEntity;
import com.ssafy.chaing.payment.domain.PaymentStatus;
import com.ssafy.chaing.payment.domain.UserPaymentEntity;
import com.ssafy.chaing.payment.repository.PaymentRepository;
import com.ssafy.chaing.payment.repository.UserPaymentRepository;
import com.ssafy.chaing.user.domain.RoleType;
import com.ssafy.chaing.user.domain.UserEntity;
import com.ssafy.chaing.user.repository.UserRepository;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@ActiveProfiles("test")
public class RentBatchServiceWithSchedulerTest {

    @TestConfiguration
    static class LocalMockConfig {
        @Primary
        @Bean
        public FintechService fintechService() {
            return mock(FintechService.class);
        }
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(
            RentBatchServiceWithSchedulerTest.class);

    @Autowired
    private FintechService fintechService;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private UserPaymentRepository userPaymentRepository;

    @Autowired
    private RentBatchService rentBatchService;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractService contractService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RentBatchConfig rentBatchConfig;

    @Autowired
    private BatchInitializer batchInitializer;

    @Autowired
    private GroupService groupService;

    private List<UserEntity> users;

    private ContractEntity contract;

    @Autowired
    private TaskScheduler scheduler;

    @BeforeEach
    void setUp() {
        // 기존에 작성한 계약 설정 메서드를 호출
        contract = setUpContract();
    }

    @Test
    void retryTaskShouldBeExecutedAutomatically() throws InterruptedException {
        // fintech 실패 세팅
        when(fintechService.rentTransfer(any())).thenReturn(new TransferDTO(false));

        // 전날 작업, 당일 작업, retry 작업 모두 짧은 시간 간격으로 바로 확인하기
        ZonedDateTime now = ZonedDateTime.now().plusSeconds(2);

        rentBatchService.setCollectTime(new ExecutionTime(now.plusSeconds(2))); // 공동 계좌 송금 → 4초 후
        rentBatchService.setPayTime(new ExecutionTime(now.plusSeconds(4)));    // 집주인 송금 → 6초 후
        rentBatchService.setRetryTime(new ExecutionTime(now.plusSeconds(6)));  // 재시도 → 8초 후

        // 계약 승인 → registerNextMonthPayment → 작업 등록
        contractService.approveContract(contract.getId(), new ApproveContractCommand(users.get(1).getId(), "111"));
        contractService.approveContract(contract.getId(), new ApproveContractCommand(users.get(2).getId(), "222"));
        contractService.approveContract(contract.getId(), new ApproveContractCommand(users.get(3).getId(), "333"));

        // 스케줄러가 자동 실행되길 기다림 (최대 6분)
        Awaitility.await()
                .atMost(30, TimeUnit.SECONDS)
                .pollDelay(Duration.ofSeconds(3))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                    PaymentEntity payment = paymentRepository.findAll().get(0);
                    assertThat(payment.getRetryCount()).isEqualTo(5);
                });
    }

    @Transactional
    public void createMultiplePaymentsForRecoveryTest() {
        createPaymentWithStatus(contract, PaymentStatus.STARTED, 0, 0, 202503);
        createPaymentWithStatus(contract, PaymentStatus.PARTIALLY_PAID, 3333, 0, 202504);
        createPaymentWithStatus(contract, PaymentStatus.COLLECTED, 10000, 0, 202505);
        createPaymentWithStatus(contract, PaymentStatus.RETRY_PENDING, 10000, 1, 202506);
        createPaymentWithStatus(contract, PaymentStatus.PAID, 10000, 0, 202507);
    }

    @Test
    void 서버_재기동_시_스케줄러_복구_검증() {
        // 1. DB에 상태별 Payment 데이터 삽입
        createMultiplePaymentsForRecoveryTest();

        // 2. BatchInitializer 수동 실행 (서버가 막 올라온 것처럼)
        batchInitializer.run(null);

        // 3. 특정 상태(PARTIALLY_PAID 등)에 대해 실행되었는지 Awaitility로 확인
        Awaitility.await()
                .atMost(30, TimeUnit.SECONDS)
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                    PaymentEntity retrying = paymentRepository.findByMonth(202506).orElseThrow();
                    assertThat(retrying.getRetryCount()).isGreaterThan(1);
                });
    }


    ContractEntity setUpContract() {
        for (int i = 1; i <= 4; i++) {
            UserEntity user = UserEntity.builder()
                    .emailAddress("test" + i + "@test.com")
                    .password(passwordEncoder.encode("password1!"))
                    .name("test" + i)
                    .nickname("test" + i)
                    .roleType(RoleType.USER)
                    .build();
            userRepository.save(user);
        }

        users = userRepository.findAll();

        // 그룹 생성 → 사용자 2번이 그룹 생성
        UserEntity creator = users.get(1);
        GroupDTO group = groupService.createGroup(new CreateGroupCommand(
                creator.getId(), "nickname", "profile", "testGroup", 3
        ));

        // 그룹 참여 → 사용자 3번, 4번 참여
        groupService.joinGroup(new JoinGroupCommand(users.get(2).getId(), group.getId(), "message1", "info1"));
        groupService.joinGroup(new JoinGroupCommand(users.get(3).getId(), group.getId(), "message2", "info2"));

        // 빈 계약 생성 → 상태는 DRAFT
        ContractDTO draftContract = contractService.createDraftContract(group.getId(), creator.getId());

        // 실제 로직과 상태 동일하게 재현하기 위해 ConfirmContractCommand 생성
        ConfirmContractCommand confirmContractCommand = new ConfirmContractCommand(
                creator.getId(), // 계약을 승인할 사용자 ID
                ZonedDateTime.now(), // 시작일
                ZonedDateTime.now().plusMonths(1), // 종료일
                new ConfirmContractCommand.ConfirmRentCommand(
                        10000, // 총액
                        10, // 납부 기한 (예: 10일)
                        "0015632899269172", // 월세 계좌 번호
                        "0012860463440599", // 소유자 계좌 번호
                        3, // 총 비율 (1:1:1)
                        List.of(
                                new ConfirmContractCommand.ConfirmRentCommand.ConfirmUserPaymentCommand(
                                        users.get(1).getId(), 3333, 1
                                ),
                                new ConfirmContractCommand.ConfirmRentCommand.ConfirmUserPaymentCommand(
                                        users.get(2).getId(), 3333, 1
                                ),
                                new ConfirmContractCommand.ConfirmRentCommand.ConfirmUserPaymentCommand(
                                        users.get(3).getId(), 3333, 1
                                )
                        )
                ),
                new ConfirmContractCommand.ConfirmUtilityCommand(null) // 카드 ID 입력
        );

        contractService.confirmContract(draftContract.getId(), confirmContractCommand);

        return contractRepository.findByIdWithMembers(draftContract.getId())
                .orElseThrow(() -> new BadRequestException("계약이 없습니다."));
    }

    private void createPaymentWithStatus(ContractEntity contract, PaymentStatus paymentStatus, int paidAmount,
                                         int retryCount, int month) {
        ZonedDateTime now = ZonedDateTime.now();
        PaymentEntity payment = PaymentEntity.builder()
                .contract(contract)
                .month(month)
                .feeType(FeeType.RENT)
                .totalAmount(10000)
                .paidAmount(paidAmount)
                .status(paymentStatus)
                .retryCount(retryCount)
                .build();
        payment.setNextExecutionDate(now.plusSeconds(10));
        paymentRepository.save(payment);

        for (ContractUserEntity member : contract.getMembers()) {
            PaymentStatus userStatus = switch (paymentStatus) {
                case STARTED -> PaymentStatus.PENDING;
                case PARTIALLY_PAID -> PaymentStatus.FAILED;
                case COLLECTED, RETRY_PENDING, PAID -> PaymentStatus.COLLECTED;
                default -> PaymentStatus.PENDING;
            };

            UserPaymentEntity userPayment = UserPaymentEntity.builder()
                    .payment(payment)
                    .contractMember(member)
                    .amount(3333)
                    .status(userStatus)
                    .build();

            userPaymentRepository.save(userPayment);
        }

        log.info("🧪 Payment 테스트 데이터 생성 완료 → status={}, month={}", paymentStatus, month);
    }
}
