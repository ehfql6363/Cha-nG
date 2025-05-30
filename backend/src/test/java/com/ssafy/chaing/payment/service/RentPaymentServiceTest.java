package com.ssafy.chaing.payment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.contract.domain.ContractStatus;
import com.ssafy.chaing.contract.domain.ContractUserEntity;
import com.ssafy.chaing.contract.domain.ContractUserStatus;
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
import com.ssafy.chaing.payment.service.command.RetrieveRentCommand;
import com.ssafy.chaing.payment.service.dto.CurrentPaymentDTO;
import com.ssafy.chaing.payment.service.dto.MonthPaymentDTO;
import com.ssafy.chaing.payment.service.dto.RetrieveRentDTO;
import com.ssafy.chaing.user.domain.UserEntity;
import com.ssafy.chaing.user.repository.UserRepository;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RentPaymentServiceTest {

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

    // 테스트에 사용될 변수들
    private Long userId;
    private UserEntity userEntity;
    private GroupEntity groupEntity;
    private ContractEntity contractEntity;
    private ContractUserEntity contractUserEntity;
    private List<PaymentEntity> payments;
    private List<UserPaymentEntity> userPayments;

    @BeforeEach
    void setUp() {
        userId = 1L;

        // 기본 유저 3명 설정
        UserEntity user1 = UserEntity.builder().id(1L).name("유저1").emailAddress("user1@example.com").build();
        UserEntity user2 = UserEntity.builder().id(2L).name("유저2").emailAddress("user2@example.com").build();
        UserEntity user3 = UserEntity.builder().id(3L).name("유저3").emailAddress("user3@example.com").build();

        userEntity = user1;

        // 그룹 생성 및 유저 그룹 설정
        groupEntity = GroupEntity.builder().id(1L).build();
        user1.setGroupId(groupEntity.getId());
        user2.setGroupId(groupEntity.getId());
        user3.setGroupId(groupEntity.getId());

        // 계약 생성 (총 월세 300만원, 각 7:2:1 비율)
        contractEntity = ContractEntity.builder()
                .id(1L)
                .group(groupEntity)
                .rentTotalAmount(3_000_000)
                .totalRentRatio(10) // 7+2+1
                .dueDate(5)
                .members(new ArrayList<>())
                .status(ContractStatus.CONFIRMED)
                .completed(true)
                .completedAt(ZonedDateTime.now(ZoneId.of("UTC")))
                .build();

        // 월세 비율 설정
        ContractUserEntity member1 = ContractUserEntity.builder()
                .id(1L)
                .user(user1)
                .contract(contractEntity)
                .rentRatio(7)
                .rentAmount(2_100_000)
                .contractStatus(ContractUserStatus.CONFIRMED)
                .build();

        contractUserEntity = member1;

        ContractUserEntity member2 = ContractUserEntity.builder()
                .id(2L)
                .user(user2)
                .contract(contractEntity)
                .rentRatio(2)
                .rentAmount(600_000)
                .contractStatus(ContractUserStatus.CONFIRMED)
                .build();

        ContractUserEntity member3 = ContractUserEntity.builder()
                .id(3L)
                .user(user3)
                .contract(contractEntity)
                .rentRatio(1)
                .rentAmount(300_000)
                .contractStatus(ContractUserStatus.CONFIRMED)
                .build();

        contractEntity.addAll(List.of(member1, member2, member3));

        // 월세 납부 내역 설정 (2025년 1월~3월)
        payments = new ArrayList<>();
        userPayments = new ArrayList<>();

        int[] months = {202501, 202502, 202503};
        for (int monthValue : months) {
            ZonedDateTime paymentDate = ZonedDateTime.of(
                    monthValue / 100, monthValue % 100, 5, 0, 0, 0, 0, ZoneId.of("UTC"));

            PaymentEntity payment = PaymentEntity.builder()
                    .id((long) monthValue)
                    .contract(contractEntity)
                    .month(monthValue)
                    .feeType(FeeType.RENT)
                    .status(PaymentStatus.PAID)
                    .paymentDate(paymentDate)
                    .totalAmount(3_000_000)
                    .paidAmount(3_000_000)
                    .allPaid(true)
                    .retryCount(0)
                    .build();

            payments.add(payment);

            userPayments.add(UserPaymentEntity.builder()
                    .id((long) (monthValue * 10 + 1))
                    .payment(payment)
                    .contractMember(member1)
                    .status(PaymentStatus.PAID)
                    .amount(2_100_000)
                    .paymentDate(paymentDate)
                    .build());

            userPayments.add(UserPaymentEntity.builder()
                    .id((long) (monthValue * 10 + 2))
                    .payment(payment)
                    .contractMember(member2)
                    .status(PaymentStatus.PAID)
                    .amount(600_000)
                    .paymentDate(paymentDate)
                    .build());

            userPayments.add(UserPaymentEntity.builder()
                    .id((long) (monthValue * 10 + 3))
                    .payment(payment)
                    .contractMember(member3)
                    .status(PaymentStatus.PAID)
                    .amount(300_000)
                    .paymentDate(paymentDate)
                    .build());
        }
    }

    @Test
    @DisplayName("retrieveRent 메소드는 사용자의 임대료 정보를 정상적으로 조회해야 한다 (모두 지불)")
    void testRetrieveRent_AllPaid() {
        // given
        RetrieveRentCommand command = new RetrieveRentCommand(userId, "2025", "3L");

        // Mock 설정
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(groupRepository.findById(groupEntity.getId())).thenReturn(Optional.of(groupEntity));
        when(contractRepository.findById(groupEntity.getContractId())).thenReturn(Optional.of(contractEntity));
        when(paymentRepository.findAllByContractIdAndFeeType(contractEntity.getId(), FeeType.RENT))
                .thenReturn(payments);
        when(contractUserRepository.findByContractIdAndUserId(contractEntity.getId(), userId))
                .thenReturn(Optional.of(contractUserEntity));

        // 현재 달의 UserPaymentEntity 업데이트 또는 추가
        List<UserPaymentEntity> updatedUserPayments = new ArrayList<>(userPayments);

        when(userPaymentRepository.findAllByPaymentIdIn(any())).thenReturn(updatedUserPayments);

        // when
        RetrieveRentDTO result = paymentService.retrieveRent(command);

        // then
        assertNotNull(result);
        assertEquals(3000000, result.getTotalAmount()); // 총 금액
        assertEquals(2100000, result.getMyAmount()); // 내 금액
        assertEquals(5, result.getDueDate()); // 납부일

        // 현재 달 결제 정보 검증
        List<CurrentPaymentDTO> currentMonth = result.getCurrentMonth();
        assertNotNull(currentMonth);
        assertEquals(3, currentMonth.size()); // 3명의 사용자

        // 각 사용자별 납부 상태 확인
        boolean foundUser1 = false, foundUser2 = false, foundUser3 = false;
        for (CurrentPaymentDTO dto : currentMonth) {
            if (dto.getUserId().equals(userId)) {
                foundUser1 = true;
                assertEquals(2100000, dto.getAmount());
                assertTrue(dto.getStatus()); // PAID 상태
            } else if (dto.getUserId().equals(2L)) {
                foundUser2 = true;
                assertEquals(600000, dto.getAmount());
                assertTrue(dto.getStatus()); // PAID 상태
            } else if (dto.getUserId().equals(3L)) {
                foundUser3 = true;
                assertEquals(300000, dto.getAmount());
                assertTrue(dto.getStatus()); // PAID 상태
            }
        }
        assertTrue(foundUser1 && foundUser2 && foundUser3);

        // 월별 결제 요약 검증
        List<MonthPaymentDTO> monthList = result.getMonthList();
        assertNotNull(monthList);
        assertEquals(3, monthList.size()); // 3개월치 데이터

        // 최신 월부터 정렬되어 있어야 함
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        String currentMonthStr = now.format(DateTimeFormatter.ofPattern("yyyyMM"));
        assertEquals("2025-3", monthList.get(0).getMonth());

        // 이번 달 납부 정보
        MonthPaymentDTO currentMonthSummary = monthList.stream().filter(m -> m.getMonth().equals("2025-3"))
                .findFirst().orElse(null);
        assertNotNull(currentMonthSummary);
        assertEquals(3, currentMonthSummary.getPaidUserIds().size());
        assertTrue(currentMonthSummary.getPaidUserIds().contains(1L));
        assertTrue(currentMonthSummary.getPaidUserIds().contains(2L));
        assertTrue(currentMonthSummary.getPaidUserIds().contains(3L));
        assertEquals(0, currentMonthSummary.getDebtUserIds().size());

        // 모든 메소드 호출 검증
        verify(userRepository).findById(userId);
        verify(paymentRepository).findAllByContractIdAndFeeType(contractEntity.getId(), FeeType.RENT);
        verify(contractUserRepository).findByContractIdAndUserId(contractEntity.getId(), userId);
        verify(userPaymentRepository).findAllByPaymentIdIn(any());
    }

    @Test
    @DisplayName("사용자가 존재하지 않는 경우 예외가 발생해야 한다")
    void testRetrieveRentWithNonExistingUser() {
        // given
        RetrieveRentCommand command = new RetrieveRentCommand(999L, "2025", "3");

        // Mock 설정
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(BadRequestException.class, () -> {
            paymentService.retrieveRent(command);
        });

        verify(userRepository).findById(999L);
    }

    @Test
    @DisplayName("그룹이 없는 경우 예외가 발생해야 한다")
    void testRetrieveRentWithNoGroup() {
        // given
        RetrieveRentCommand command = new RetrieveRentCommand(userId, "2025", "3");
        UserEntity userWithoutGroup = UserEntity.builder()
                .id(userId)
                .emailAddress("test@example.com")
                .name("Test User")
                .build();

        // Mock 설정
        when(userRepository.findById(userId)).thenReturn(Optional.of(userWithoutGroup));
        when(groupRepository.findById(null)).thenReturn(Optional.empty());

        // when & then
        assertThrows(BadRequestException.class, () -> {
            paymentService.retrieveRent(command);
        });

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("계약서가 없는 경우 예외가 발생해야 한다")
    void testRetrieveRentWithNoContract() {
        // given
        RetrieveRentCommand command = new RetrieveRentCommand(userId, "2025", "3");

        // Mock 설정
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(groupRepository.findById(groupEntity.getId())).thenReturn(Optional.of(groupEntity));
        when(contractRepository.findById(groupEntity.getContractId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(BadRequestException.class, () -> {
            paymentService.retrieveRent(command);
        });

        verify(userRepository).findById(userId);
        verify(groupRepository).findById(groupEntity.getId());
        verify(contractRepository).findById(groupEntity.getContractId());
    }
}