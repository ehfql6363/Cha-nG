package com.ssafy.chaing.payment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.contract.domain.ContractUserEntity;
import com.ssafy.chaing.contract.repository.ContractRepository;
import com.ssafy.chaing.contract.repository.ContractUserRepository;
import com.ssafy.chaing.group.domain.GroupEntity;
import com.ssafy.chaing.group.repository.GroupRepository;
import com.ssafy.chaing.payment.domain.FeeType;
import com.ssafy.chaing.payment.domain.PaymentEntity;
import com.ssafy.chaing.payment.domain.PaymentStatus;
import com.ssafy.chaing.payment.domain.UserPaymentEntity;
import com.ssafy.chaing.payment.repository.PaymentRepository;
import com.ssafy.chaing.payment.repository.UserPaymentRepository;
import com.ssafy.chaing.payment.service.command.RetrieveUtilityCommand;
import com.ssafy.chaing.payment.service.dto.CurrentPaymentDTO;
import com.ssafy.chaing.payment.service.dto.RetrieveUtilityDTO;
import com.ssafy.chaing.payment.service.dto.WeekPaymentDTO;
import com.ssafy.chaing.user.domain.UserEntity;
import com.ssafy.chaing.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
public class UtilityPaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ContractUserRepository contractUserRepository;

    @Mock
    private UserPaymentRepository userPaymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    // 테스트에 사용할 기본 데이터
    private Long userId;
    private String year;
    private String month;
    private Long contractId;
    private Long groupId;

    private UserEntity userEntity;
    private GroupEntity groupEntity;
    private ContractEntity contractEntity;
    private ContractUserEntity contractUserEntity;
    private List<PaymentEntity> paymentEntities;
    private List<UserPaymentEntity> userPaymentEntities;

    @BeforeEach
    void setUp() {
        // 기본 데이터 설정
        userId = 1L;
        year = "2025";
        month = "03";
        contractId = 10L;
        groupId = 100L;

        // 엔티티 설정
        userEntity = mock(UserEntity.class);
        lenient().when(userEntity.getId()).thenReturn(userId);
        lenient().when(userEntity.getGroupId()).thenReturn(groupId);

        groupEntity = mock(GroupEntity.class);
        lenient().when(groupEntity.getContractId()).thenReturn(contractId);

        contractEntity = mock(ContractEntity.class);
        lenient().when(contractEntity.getId()).thenReturn(contractId);
        lenient().when(contractEntity.getRentTotalAmount()).thenReturn(1000000);

        contractUserEntity = mock(ContractUserEntity.class);

        // PaymentEntity 설정
        PaymentEntity payment1 = mock(PaymentEntity.class);
        lenient().when(payment1.getId()).thenReturn(1001L);
        lenient().when(payment1.getMonth()).thenReturn(202503); // yyyyMM 형식
        lenient().when(payment1.getWeek()).thenReturn(1);

        PaymentEntity payment2 = mock(PaymentEntity.class);
        lenient().when(payment2.getId()).thenReturn(1002L);
        lenient().when(payment2.getMonth()).thenReturn(202503);
        lenient().when(payment2.getWeek()).thenReturn(2);

        PaymentEntity payment3 = mock(PaymentEntity.class);
        lenient().when(payment3.getId()).thenReturn(1003L);
        lenient().when(payment3.getMonth()).thenReturn(202503);
        lenient().when(payment3.getWeek()).thenReturn(3);

        paymentEntities = Arrays.asList(payment3, payment2, payment1); // 주 내림차순 정렬 (3, 2, 1)

        // UserPaymentEntity 모킹 및 설정
        UserPaymentEntity userPayment1 = mock(UserPaymentEntity.class);
        UserPaymentEntity userPayment2 = mock(UserPaymentEntity.class);
        UserPaymentEntity userPayment3 = mock(UserPaymentEntity.class);

        // 사용자 결제 정보 설정
        setupUserPaymentEntity(userPayment1, payment1, userId, 50000, PaymentStatus.PAID);
        setupUserPaymentEntity(userPayment2, payment2, userId, 60000, PaymentStatus.DEBT);
        setupUserPaymentEntity(userPayment3, payment3, userId, 70000, PaymentStatus.PAID);

        userPaymentEntities = Arrays.asList(userPayment1, userPayment2, userPayment3);
    }

    private void setupUserPaymentEntity(UserPaymentEntity userPaymentEntity,
                                        PaymentEntity payment,
                                        Long userId,
                                        int amount,
                                        PaymentStatus status) {
        ContractUserEntity contractUser = mock(ContractUserEntity.class);
        UserEntity user = mock(UserEntity.class);
        lenient().when(user.getId()).thenReturn(userId);
        lenient().when(contractUser.getUser()).thenReturn(user);

        lenient().when(userPaymentEntity.getContractMember()).thenReturn(contractUser);
        lenient().when(userPaymentEntity.getPayment()).thenReturn(payment);
        lenient().when(userPaymentEntity.getAmount()).thenReturn(amount);
        lenient().when(userPaymentEntity.getStatus()).thenReturn(status);
    }

    @Test
    @DisplayName("공과금 납부 정보 조회 테스트 - 정상 케이스")
    void retrieveUtility_NormalCase_ShouldReturnCorrectData() {
        // Given
        RetrieveUtilityCommand command = new RetrieveUtilityCommand(userId, year, month);
        int formattedMonth = 202503; // year + month 형식

        // 모킹 설정
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(groupEntity));
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contractEntity));
        when(contractUserRepository.findByContractIdAndUserId(contractId, userId))
                .thenReturn(Optional.of(contractUserEntity));

        when(paymentRepository.findAllByContractIdAndFeeTypeAndMonthOrderByWeekDesc(
                contractId, FeeType.UTILITY, formattedMonth))
                .thenReturn(paymentEntities);

//        List<Long> paymentIds = Arrays.asList(1001L, 1002L, 1003L);
        when(userPaymentRepository.findAllByPaymentIdIn(any()))
                .thenReturn(userPaymentEntities);

        // When
        RetrieveUtilityDTO result = paymentService.retrieveUtility(command);

        // Then
        assertNotNull(result);
        assertEquals(1000000, result.getTotalAmount()); // 계약서에 명시된 렌트 총액
        assertEquals(70000, result.getMyAmount()); // 최신 주(3주)의 내 금액

        // 최신 주의 결제 정보 확인
        List<CurrentPaymentDTO> currentWeekPayments = result.getCurrentWeek();
        assertNotNull(currentWeekPayments);
        assertEquals(1, currentWeekPayments.size());

        // 주별 결제 요약 확인
        List<WeekPaymentDTO> weekList = result.getWeekList();
        assertNotNull(weekList);
        assertEquals(3, weekList.size()); // 3개의 주 데이터

        // 주별 정렬 확인 (내림차순)
        assertEquals(3, weekList.get(0).getWeek());
        assertEquals(2, weekList.get(1).getWeek());
        assertEquals(1, weekList.get(2).getWeek());
    }

    @Test
    @DisplayName("공과금 납부 정보 조회 테스트 - 사용자 없는 경우")
    void retrieveUtility_UserNotFound_ShouldThrowException() {
        // Given
        RetrieveUtilityCommand command = new RetrieveUtilityCommand(userId, year, month);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BadRequestException.class, () -> paymentService.retrieveUtility(command));
    }

    @Test
    @DisplayName("공과금 납부 정보 조회 테스트 - 그룹 없는 경우")
    void retrieveUtility_GroupNotFound_ShouldThrowException() {
        // Given
        RetrieveUtilityCommand command = new RetrieveUtilityCommand(userId, year, month);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BadRequestException.class, () -> paymentService.retrieveUtility(command));
    }

    @Test
    @DisplayName("공과금 납부 정보 조회 테스트 - 계약서 없는 경우")
    void retrieveUtility_ContractNotFound_ShouldThrowException() {
        // Given
        RetrieveUtilityCommand command = new RetrieveUtilityCommand(userId, year, month);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(groupEntity));
        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BadRequestException.class, () -> paymentService.retrieveUtility(command));
    }

    @Test
    @DisplayName("공과금 납부 정보 조회 테스트 - 해당 월에 데이터 없는 경우")
    void retrieveUtility_NoDataForMonth_ShouldReturnEmptyLists() {
        // Given
        RetrieveUtilityCommand command = new RetrieveUtilityCommand(userId, year, month);
        int formattedMonth = 202503;

        // 모킹 설정
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(groupEntity));
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contractEntity));
        when(contractUserRepository.findByContractIdAndUserId(contractId, userId))
                .thenReturn(Optional.of(contractUserEntity));

        // 빈 결제 리스트 반환
        when(paymentRepository.findAllByContractIdAndFeeTypeAndMonthOrderByWeekDesc(
                contractId, FeeType.UTILITY, formattedMonth))
                .thenReturn(Collections.emptyList());

        when(userPaymentRepository.findAllByPaymentIdIn(anyList()))
                .thenReturn(Collections.emptyList());

        // When
        RetrieveUtilityDTO result = paymentService.retrieveUtility(command);

        // Then
        assertNotNull(result);
        assertEquals(1000000, result.getTotalAmount());
        assertEquals(0, result.getMyAmount()); // 데이터 없으므로 0

        // 빈 리스트 확인
        assertTrue(result.getCurrentWeek().isEmpty());
        assertTrue(result.getWeekList().isEmpty());
    }

    @Test
    @DisplayName("공과금 납부 정보 조회 테스트 - 명령어가 null인 경우")
    void retrieveUtility_NullCommand_ShouldThrowException() {
        // When & Then
        assertThrows(NullPointerException.class, () -> paymentService.retrieveUtility(null));
    }
}