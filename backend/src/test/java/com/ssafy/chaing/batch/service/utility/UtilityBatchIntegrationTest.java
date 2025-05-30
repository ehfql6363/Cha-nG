package com.ssafy.chaing.batch.service.utility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.contract.domain.ContractStatus;
import com.ssafy.chaing.contract.domain.ContractUserEntity;
import com.ssafy.chaing.contract.domain.ContractUserStatus;
import com.ssafy.chaing.contract.domain.UtilityCardEntity;
import com.ssafy.chaing.contract.repository.ContractRepository;
import com.ssafy.chaing.contract.repository.ContractUserRepository;
import com.ssafy.chaing.contract.repository.UtilityCardRepository;
import com.ssafy.chaing.fintech.controller.request.InquireBillingCommand;
import com.ssafy.chaing.fintech.dto.InquireBillingStatementsRec;
import com.ssafy.chaing.fintech.dto.InquireBillingStatementsRec.BillingStatementItem;
import com.ssafy.chaing.fintech.service.FintechService;
import com.ssafy.chaing.payment.domain.PaymentEntity;
import com.ssafy.chaing.payment.domain.UserPaymentEntity;
import com.ssafy.chaing.payment.repository.PaymentRepository;
import com.ssafy.chaing.payment.repository.UserPaymentRepository;
import com.ssafy.chaing.user.domain.RoleType;
import com.ssafy.chaing.user.domain.UserEntity;
import com.ssafy.chaing.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBatchTest // Spring Batch 테스트 유틸리티 활성화
@SpringBootTest  // Spring Boot 통합 테스트 환경 로드
@ActiveProfiles("test") // application-test.yml 설정 사용
class UtilityBatchIntegrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    // 테스트할 Job을 주입받습니다. Configuration 클래스에서 정의한 Job Bean 이름을 사용합니다.
    @Autowired
    @Qualifier("utilityBillingStatementJob") // 동일 타입 Job이 여러개일 경우 Qualifier 명시
    private Job utilityBillingStatementJob;

    // 실제 외부 API 호출 대신 Mock 객체 사용
    @MockitoBean
    private FintechService fintechService;

    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractUserRepository contractUserRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserPaymentRepository userPaymentRepository;
    @Autowired
    private UtilityCardRepository utilityCardRepository;

    // 각 테스트 전에 데이터 정리 및 Mock 설정
    @BeforeEach
    void setUp() {
        // 테스트 실행 전 관련 테이블 데이터 삭제 (실행 순서 중요 - FK 제약조건 고려)
        userPaymentRepository.deleteAllInBatch();
        paymentRepository.deleteAllInBatch();
        contractUserRepository.deleteAllInBatch();
        // UtilityCard는 Contract 삭제 시 Cascade 설정에 따라 삭제될 수 있음 (설정 확인 필요)
        contractRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        // JobLauncherTestUtils에 테스트 대상 Job 설정
        jobLauncherTestUtils.setJob(utilityBillingStatementJob);
    }

    // 각 테스트 후 데이터 정리 (선택적, @Transactional 사용 시 불필요할 수 있음)
    @AfterEach
    void tearDown() {
        // @Transactional을 사용하지 않는 경우, 여기서 명시적으로 데이터 정리
        // userRepository.deleteAllInBatch(); 등등...
    }

    @Test
    @DisplayName("정상 케이스: 멤버가 있는 계약의 청구내역을 성공적으로 저장하고 Job이 COMPLETED 된다")
    void shouldSaveBillingStatementsAndCompleteJob_WhenContractHasMembers() throws Exception {
        // given: 테스트 데이터 설정
        // 1. 사용자 생성
        UserEntity user1 = userRepository.save(
                UserEntity.builder()
                        .name("김동열")
                        .emailAddress("ehfql6363@naver.com")
                        .roleType(RoleType.USER)
                        .password("password")
                        .nickname("여뤼")
                        .profileImage("image")
                        .build()
        );
        UserEntity user2 = userRepository.save(
                UserEntity.builder()
                        .name("이동현")
                        .emailAddress("sky어쩌고@naver.com")
                        .roleType(RoleType.USER)
                        .password("password")
                        .nickname("곤듀")
                        .profileImage("image")
                        .build()
        );
        UserEntity user3 = userRepository.save(
                UserEntity.builder()
                        .name("Chaing")
                        .emailAddress("admin@admin.com")
                        .roleType(RoleType.ADMIN)
                        .password("password")
                        .nickname("사장")
                        .profileImage("image")
                        .build()
        );

        // 2. 계약 및 카드 생성
        UtilityCardEntity card = UtilityCardEntity.builder()
                .cardNo("1002350637289391")
                .cvc("707")
                .build();
        utilityCardRepository.save(card);
        ContractEntity contract = ContractEntity.builder()
                .utilityCard(card)
                .utilityRatio(2)
                .status(ContractStatus.CONFIRMED)
                .members(new ArrayList<>())
                .build();
        contractRepository.save(contract); // Contract 저장 시 Card도 Cascade PERSIST/MERGE 설정 필요

        // 3. 계약 멤버 생성 (isSurplusUser 설정 포함)
        ContractUserEntity member1 = ContractUserEntity.builder()
                .contract(contract)
                .user(user1)
                .isSurplusUser(false)
                .contractStatus(ContractUserStatus.CONFIRMED)
                .build(); // 나머지를 받을 사용자
        ContractUserEntity member2 = ContractUserEntity.builder()
                .contract(contract)
                .user(user2)
                .isSurplusUser(false)
                .contractStatus(ContractUserStatus.CONFIRMED)
                .build();
        ContractUserEntity member3 = ContractUserEntity.builder()
                .contract(contract)
                .user(user3)
                .isSurplusUser(true)
                .contractStatus(ContractUserStatus.CONFIRMED)
                .build();
        contractUserRepository.saveAll(List.of(member1, member2, member3));
        contract.getMembers().addAll(List.of(member1, member2, member3));

        // 4. FintechService Mock 설정
        String cardNo = card.getCardNo();
        String cvc = card.getCvc();
        // given: FintechService Mock 설정 (제공된 DTO 및 JSON 구조 기반)
        String responseBillingMonth = "202503"; // 응답 DTO의 billingMonth (YYYYMM 형식)

        // BillingStatementItem 리스트 생성 (JSON의 billingList 반영, 값은 String)
        BillingStatementItem item1 = new BillingStatementItem("4", "20250324", "17100", "결제완료", "20250326", "160001");
        BillingStatementItem item2_last = new BillingStatementItem("5", "20250331", "8100", "미결제", "",
                ""); // 이 항목이 처리 대상

        List<BillingStatementItem> billingItems = List.of(item1, item2_last);

        // 최종 Mock 응답 객체 생성 (단일 InquireBillingStatementsRec 반환 가정)
        InquireBillingStatementsRec mockResponse = new InquireBillingStatementsRec(responseBillingMonth, billingItems);

        // Mockito 설정: 어떤 InquireBillingCommand가 오든 위에서 만든 단일 객체를 반환
        when(fintechService.inquireBillingStatements(ArgumentMatchers.any(InquireBillingCommand.class)))
                .thenReturn(List.of(mockResponse));

        // when: Job 실행
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()) // JobInstance 구분을 위한 파라미터
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then: 결과 검증
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        // DB 상태 검증 (저장된 값은 정수형으로 변환되었다고 가정)
        List<PaymentEntity> payments = paymentRepository.findAll();
        List<UserPaymentEntity> userPayments = userPaymentRepository.findAll();

        assertThat(payments).hasSize(1);
        PaymentEntity savedPayment = payments.getFirst();

        // CurrentBillingStatementDTO가 파싱했을 것으로 예상되는 값으로 검증
        assertThat(savedPayment.getTotalAmount()).isEqualTo(8100);
        assertThat(savedPayment.getMonth()).isEqualTo(202503);
        assertThat(savedPayment.getWeek()).isEqualTo(5);

        assertThat(userPayments).hasSize(3);
        // 8100 / 2 = 4050, 나머지 0
        UserPaymentEntity surplusUserPayment = userPayments.stream()
                .filter(up -> up.getContractMember().isSurplusUser()).findFirst().orElseThrow();
        UserPaymentEntity otherUserPayment = userPayments.stream().filter(up -> !up.getContractMember().isSurplusUser())
                .findFirst().orElseThrow();
        assertThat(surplusUserPayment.getAmount()).isEqualTo(0);
        assertThat(otherUserPayment.getAmount()).isEqualTo(4050);
    }

    @Test
    @DisplayName("엣지 케이스: 계약에 멤버가 없으면 Payment가 생성되지 않고 Job은 COMPLETED 된다")
    void shouldNotCreatePaymentAndCompleteJob_WhenContractHasNoMembers() throws Exception {
        // given: 멤버가 없는 계약 설정
        UtilityCardEntity card = UtilityCardEntity.builder().cardNo("1002350637289391").cvc("707").build();
        utilityCardRepository.save(card);
        ContractEntity contract = ContractEntity.builder()
                .utilityCard(card)
                .utilityRatio(2)
                .status(ContractStatus.CONFIRMED)
                .build();
        contractRepository.save(contract);

        // given: FintechService Mock 설정 (billingList가 빈 리스트인 응답)
        String responseBillingMonth = "202504";
        InquireBillingStatementsRec mockResponseWithEmptyList = new InquireBillingStatementsRec(responseBillingMonth,
                List.of()); // billingList가 비어있음

        when(fintechService.inquireBillingStatements(ArgumentMatchers.any(InquireBillingCommand.class)))
                .thenReturn(List.of(mockResponseWithEmptyList));

        // when: Job 실행
        JobParameters jobParameters = new JobParametersBuilder().addLong("emptyListTime", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then: 결과 검증
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED); // Job 자체는 성공
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        // DB 검증 (Payment 생성 안됨)
        assertThat(paymentRepository.count()).isZero();
        assertThat(userPaymentRepository.count()).isZero();
    }
}
