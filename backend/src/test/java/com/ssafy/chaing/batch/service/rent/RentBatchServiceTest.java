package com.ssafy.chaing.batch.service.rent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ssafy.chaing.batch.config.RentBatchConfig;
import com.ssafy.chaing.batch.config.BatchInitializer;
import com.ssafy.chaing.batch.service.RentBatchService;
import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.contract.repository.ContractRepository;
import com.ssafy.chaing.contract.service.ContractService;
import com.ssafy.chaing.contract.service.command.ApproveContractCommand;
import com.ssafy.chaing.contract.service.command.ConfirmContractCommand;
import com.ssafy.chaing.contract.service.dto.ContractDTO;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@ActiveProfiles("test")
@SpringBootTest
public class RentBatchServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractService contractService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private RentBatchService rentBatchService;

    @Autowired
    private UserPaymentRepository userPaymentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RentBatchConfig rentBatchConfig;

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private ContractEntity contract;

    @MockitoSpyBean
    private TaskScheduler taskScheduler;

    private List<UserEntity> users;

    @BeforeEach
    void setUp() {
        // 기존에 작성한 계약 설정 메서드를 호출
        contract = setUpContract();

        // TaskScheduler Mock 처리
        doAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            task.run(); // 스케줄된 작업 즉시 실행
            return null;
        }).when(taskScheduler).schedule(any(Runnable.class), any(Instant.class));
    }

    @Test
    void testRegisterNextMonthPayment() {

        contractService.approveContract(contract.getId(),
                new ApproveContractCommand(users.get(1).getId(), "0016876352742020"));

        contractService.approveContract(contract.getId(),
                new ApproveContractCommand(users.get(2).getId(), "0019468386865145"));

        contractService.approveContract(contract.getId(),
                new ApproveContractCommand(users.get(3).getId(), "0010624269496821"));

        // Awaitility로 비동기 작업이 완료될 때까지 최대 5초까지 대기
        Awaitility.await().untilAsserted(() -> {
            PaymentEntity payment = paymentRepository.findAll().get(0);

            // 공동계좌 송금 완료 확인
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PAID);
            assertThat(payment.getPaidAmount()).isEqualTo(payment.getTotalAmount());
        });
    }

    @Test
    void testPartialPaymentWhenOneUserFails() {

        contractService.approveContract(contract.getId(),
                new ApproveContractCommand(users.get(1).getId(), "0016876352742020"));

        // 유효하지 않은 계좌 넣기
        contractService.approveContract(contract.getId(),
                new ApproveContractCommand(users.get(2).getId(), "2122222222"));

        contractService.approveContract(contract.getId(),
                new ApproveContractCommand(users.get(3).getId(), "0010624269496821"));

        Awaitility.await().untilAsserted(() -> {
            PaymentEntity payment = paymentRepository.findAll().get(0);

            // 상태가 PARTIALLY_PAID인지 확인
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PARTIALLY_PAID);

            // 유저 송금 내역 상태 확인
            List<UserPaymentEntity> userPayments = userPaymentRepository.findWithMemberAndUserByPaymentId(
                    payment.getId());
            long failedCount = userPayments.stream()
                    .filter(p -> p.getStatus() == PaymentStatus.FAILED)
                    .count();
            long successCount = userPayments.stream()
                    .filter(p -> p.getStatus() == PaymentStatus.COLLECTED)
                    .count();

            assertThat(failedCount).isEqualTo(1);
            assertThat(successCount).isEqualTo(3); // 나머지 유저를 포함
        });
    }

    @Test
    void testAllPaymentStatusesScheduleCount() {
        // 1. 유저 승인
        contractService.approveContract(contract.getId(),
                new ApproveContractCommand(users.get(1).getId(), "0016876352742020"));
        contractService.approveContract(contract.getId(),
                new ApproveContractCommand(users.get(2).getId(), "0019468386865145"));
        contractService.approveContract(contract.getId(),
                new ApproveContractCommand(users.get(3).getId(), "0010624269496821"));

        // 2. 각 상태에 대해 PaymentEntity 생성
        savePaymentWithStatus(PaymentStatus.STARTED);         // 2번 등록 (collect + owner)
        savePaymentWithStatus(PaymentStatus.PARTIALLY_PAID);  // 1번 등록 (owner)
        savePaymentWithStatus(PaymentStatus.COLLECTED);       // 1번 등록 (owner)
        savePaymentWithStatus(PaymentStatus.RETRY_PENDING);   // 1번 등록 (owner)
        savePaymentWithStatus(PaymentStatus.PAID);            // ❌ 등록 X
        savePaymentWithStatus(PaymentStatus.FAILED);          // ❌ 등록 X

        // 3. 즉시 실행되게 설정
        doAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            task.run();
            return null;
        }).when(taskScheduler).schedule(any(Runnable.class), any(Instant.class));

        // 4. 복구 로직 실행
        new BatchInitializer(rentBatchConfig).run(null);

        // 5. TaskScheduler 호출 횟수 검증
        // - STARTED 2번 →  2 * 2 = 4번 (collect + pay)
        // - PARTIALLY_PAID, COLLECTED, RETRY_PENDING → 각 1번 (pay만)
        // 총 5번이 되어야 정상
        verify(taskScheduler, times(7)).schedule(any(Runnable.class), any(Instant.class));
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

        return contractRepository.findById(draftContract.getId())
                .orElseThrow(() -> new BadRequestException("계약이 없습니다."));
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

}
