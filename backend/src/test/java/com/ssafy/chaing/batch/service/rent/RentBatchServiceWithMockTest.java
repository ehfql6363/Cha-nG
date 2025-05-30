package com.ssafy.chaing.batch.service.rent;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ssafy.chaing.batch.config.RentBatchConfig;
import com.ssafy.chaing.batch.service.RentBatchService;
import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.contract.repository.ContractRepository;
import com.ssafy.chaing.contract.service.ContractService;
import com.ssafy.chaing.contract.service.command.ApproveContractCommand;
import com.ssafy.chaing.contract.service.command.ConfirmContractCommand;
import com.ssafy.chaing.contract.service.dto.ContractDTO;
import com.ssafy.chaing.fintech.controller.request.TransferCommand;
import com.ssafy.chaing.fintech.service.FintechService;
import com.ssafy.chaing.fintech.service.dto.TransferDTO;
import com.ssafy.chaing.group.service.GroupService;
import com.ssafy.chaing.group.service.command.CreateGroupCommand;
import com.ssafy.chaing.group.service.command.JoinGroupCommand;
import com.ssafy.chaing.group.service.dto.GroupDTO;
import com.ssafy.chaing.payment.domain.FeeType;
import com.ssafy.chaing.payment.domain.PaymentEntity;
import com.ssafy.chaing.payment.domain.PaymentStatus;
import com.ssafy.chaing.payment.repository.PaymentRepository;
import com.ssafy.chaing.user.domain.RoleType;
import com.ssafy.chaing.user.domain.UserEntity;
import com.ssafy.chaing.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class RentBatchServiceWithMockTest {

    @TestConfiguration
    static class LocalMockConfig {
        @Primary
        @Bean
        public FintechService fintechService() {
            return mock(FintechService.class);
        }

        @Primary
        @Bean
        public TaskScheduler taskScheduler() {
            return mock(TaskScheduler.class);
        }
    }

    @Autowired
    private EntityManager em;

    @Autowired
    private FintechService fintechService;

    @Autowired
    private TaskScheduler taskScheduler;

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
    private GroupService groupService;

    private List<UserEntity> users;

    private ContractEntity contract;

    @BeforeEach
    void setUp() {
        // 기존에 작성한 계약 설정 메서드를 호출
        contract = setUpContract();
    }

    @Test
    void testRecoveryWithManualExecutionOfTasks() {

        // Step 0: TaskScheduler 미리 설정 (Runnable 캡처 + 스케줄은 실행 안되게)
        ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
        when(taskScheduler.schedule(taskCaptor.capture(), any(Instant.class)))
                .thenReturn(null); // 혹은 mock(ScheduledFuture.class) 반환 가능

        // Step 1: fintechService - 두 번째 유저만 실패하도록 세팅
        when(fintechService.rentTransfer(any())).thenAnswer(invocation -> {
            TransferCommand cmd = invocation.getArgument(0);
            return cmd.getFromAccountNo().equals("0019468386865145")
                    ? new TransferDTO(false) // 실패
                    : new TransferDTO(true); // 성공
        });

        // Step 2: 유저 승인 (→ 내부적으로 registerNextMonthPayment 호출)
        contractService.approveContract(contract.getId(),
                new ApproveContractCommand(users.get(1).getId(), "0016876352742020"));
        contractService.approveContract(contract.getId(),
                new ApproveContractCommand(users.get(2).getId(), "0019468386865145"));
        contractService.approveContract(contract.getId(),
                new ApproveContractCommand(users.get(3).getId(), "0010624269496821"));

        // Step 3: 등록된 작업들 수동 실행
        List<Runnable> scheduledTasks = taskCaptor.getAllValues();
        Runnable collectTask = scheduledTasks.get(0); // 전날 작업
        Runnable ownerTask = scheduledTasks.get(1);   // 당일 작업

        // Step 4: 전날 작업 수동 실행 (→ 일부 실패 유도)
        collectTask.run();

        Awaitility.await().untilAsserted(() -> {
            PaymentEntity payment = paymentRepository.findAll().get(0);
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PARTIALLY_PAID);
        });

        // Step 5: fintechService → 전체 성공으로 다시 세팅
        Mockito.reset(fintechService);
        when(fintechService.rentTransfer(any())).thenReturn(new TransferDTO(true));

        // Step 6: 당일 작업 수동 실행 (→ 최종 송금 시도)
        ownerTask.run();

        // Step 7: 최종 상태는 PAID여야 함
        Awaitility.await().untilAsserted(() -> {
            PaymentEntity payment = paymentRepository.findAll().get(0);
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PAID);
            assertThat(payment.getPaidAmount()).isEqualTo(payment.getTotalAmount());
        });
    }

    @Test
    void testRetrySchedulingWhenOwnerTransferFails() {
        // Step 1: fintechService 설정 → 집주인 송금 실패
        when(fintechService.rentTransfer(any())).thenReturn(new TransferDTO(false));

        // Step 2: TaskScheduler Mock → Runnable 캡처
        ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
        when(taskScheduler.schedule(taskCaptor.capture(), any(Instant.class)))
                .thenReturn(null);

        // Step 3: 유저 승인 (→ 자동 registerNextMonthPayment 호출)
        contractService.approveContract(contract.getId(), new ApproveContractCommand(users.get(1).getId(), "111"));
        contractService.approveContract(contract.getId(), new ApproveContractCommand(users.get(2).getId(), "222"));
        contractService.approveContract(contract.getId(), new ApproveContractCommand(users.get(2).getId(), "222"));
        contractService.approveContract(contract.getId(), new ApproveContractCommand(users.get(3).getId(), "333"));

        // Step 4: 수동으로 전날 collect 작업 먼저 성공 처리
        List<Runnable> scheduledTasks = taskCaptor.getAllValues();
        Runnable collectTask = scheduledTasks.get(0);
        Runnable ownerTask = scheduledTasks.get(1);

        // Step 5: fintechService 재설정 → collect는 성공하게
        reset(fintechService);
        when(fintechService.rentTransfer(any())).thenReturn(new TransferDTO(true));
        collectTask.run();

        // Step 6: 다시 실패 설정 → payToOwner는 실패
        reset(fintechService);
        when(fintechService.rentTransfer(any())).thenAnswer(invocation -> {
            return new TransferDTO(false); // 집주인 송금 실패 유도
        });

        ownerTask.run();
        paymentRepository.flush();

        // Step 7: 상태 확인 (RETRY_PENDING)
        Awaitility.await().untilAsserted(() -> {
            PaymentEntity payment = paymentRepository.findAll().get(0);
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.RETRY_PENDING);
            assertThat(payment.getRetryCount()).isEqualTo(1);
        });

        // Step 8: 재시도 스케줄러 등록 확인
        verify(taskScheduler, times(3)).schedule(any(Runnable.class), any(Instant.class));
    }

    void savePaymentWithStatus(PaymentStatus status) {
        PaymentEntity payment = PaymentEntity.builder()
                .contract(contract)
                .month(202503)
                .feeType(FeeType.RENT)
                .totalAmount(10000)
                .status(status)
                .paidAmount(0)
                .build();

        payment.setNextExecutionDate(ZonedDateTime.now().plusSeconds(2));
        paymentRepository.save(payment);
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

}
