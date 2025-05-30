package com.ssafy.chaing.payment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.payment.controller.request.RetrieveRentRequest;
import com.ssafy.chaing.payment.controller.request.RetrieveUtilityRequest;
import com.ssafy.chaing.payment.service.PaymentService;
import com.ssafy.chaing.payment.service.command.RetrieveRentCommand;
import com.ssafy.chaing.payment.service.command.RetrieveUtilityCommand;
import com.ssafy.chaing.payment.service.dto.CurrentPaymentDTO;
import com.ssafy.chaing.payment.service.dto.MonthPaymentDTO;
import com.ssafy.chaing.payment.service.dto.RetrieveRentDTO;
import com.ssafy.chaing.payment.service.dto.RetrieveUtilityDTO;
import com.ssafy.chaing.payment.service.dto.WeekPaymentDTO;
import com.ssafy.chaing.user.domain.RoleType;
import com.ssafy.chaing.user.domain.UserEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    private UserEntity user;
    private RetrieveRentDTO mockRentDTO;
    private RetrieveUtilityDTO mockUtilityDTO;

    @BeforeEach
    void setUp() {
        // 유저 정보 설정
        user = UserEntity.builder()
                .id(1L)
                .emailAddress("test0@test.com")
                .password("password1!")
                .name("test")
                .roleType(RoleType.USER)
                .build();

        // 임대료 정보 모의 데이터 설정
        List<CurrentPaymentDTO> currentMonthPayments = new ArrayList<>();
        currentMonthPayments.add(new CurrentPaymentDTO(1L, 2100000, true));
        currentMonthPayments.add(new CurrentPaymentDTO(2L, 600000, true));
        currentMonthPayments.add(new CurrentPaymentDTO(3L, 300000, true));

        List<MonthPaymentDTO> monthList = new ArrayList<>();
        monthList.add(new MonthPaymentDTO("2025-3", List.of(1L, 2L, 3L), List.of()));
        monthList.add(new MonthPaymentDTO("2025-2", List.of(1L, 3L), List.of(2L)));

        mockRentDTO = new RetrieveRentDTO(3000000, 2100000, 5, currentMonthPayments, monthList);

        // 공과금 정보 모의 데이터 설정
        List<CurrentPaymentDTO> currentWeekPayments = new ArrayList<>();
        currentWeekPayments.add(new CurrentPaymentDTO(1L, 3333, true));
        currentWeekPayments.add(new CurrentPaymentDTO(2L, 3333, false));
        currentWeekPayments.add(new CurrentPaymentDTO(3L, 3333, true));

        List<WeekPaymentDTO> weekList = new ArrayList<>();
        weekList.add(new WeekPaymentDTO("2025-3", 3, 10000, List.of(1L, 3L), List.of(2L)));
        weekList.add(new WeekPaymentDTO("2025-3", 2, 10000, List.of(1L, 2L, 3L), List.of()));
        weekList.add(new WeekPaymentDTO("2025-3", 1, 10000, List.of(1L, 2L, 3L), List.of()));

        mockUtilityDTO = new RetrieveUtilityDTO(10000, 3333, currentWeekPayments, weekList);
    }

    @Test
    @DisplayName("임대료 조회 API 테스트")
    void retrieveRentTest() throws Exception {
        // given
        // Authentication 객체 생성
        UserPrincipal userPrincipal = new UserPrincipal(
                String.valueOf(user.getId()), // setUp에서 만든 user 객체의 ID 사용
                "password", // 더미 비밀번호
                Set.of(new SimpleGrantedAuthority("ROLE_USER")) // 실제 역할 사용
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );

        RetrieveRentRequest request = new RetrieveRentRequest("2025-3");
        when(paymentService.retrieveRent(any(RetrieveRentCommand.class))).thenReturn(mockRentDTO);

        // when & then
        mockMvc.perform(get("/api/v1/payment/rent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalAmount").value(3000000))
                .andExpect(jsonPath("$.data.myAmount").value(2100000))
                .andExpect(jsonPath("$.data.dueDate").value(5))
                .andExpect(jsonPath("$.data.currentMonth[0].userId").value(1L))
                .andExpect(jsonPath("$.data.currentMonth[0].amount").value(2100000))
                .andExpect(jsonPath("$.data.currentMonth[0].status").value(true))
                .andExpect(jsonPath("$.data.monthList[0].month").value("2025-3"))
                .andExpect(jsonPath("$.data.monthList[0].paidUserIds").isArray())
                .andExpect(jsonPath("$.data.monthList[0].paidUserIds[0]").value(1L))
                .andExpect(jsonPath("$.data.monthList[0].paidUserIds[1]").value(2L))
                .andExpect(jsonPath("$.data.monthList[0].paidUserIds[2]").value(3L));
    }

    @Test
    @DisplayName("임대료 조회 서비스 예외 처리 테스트")
    void retrieveRentServiceExceptionTest() throws Exception {
        // given
        // Authentication 객체 생성
        UserPrincipal userPrincipal = new UserPrincipal(
                String.valueOf(user.getId()), // setUp에서 만든 user 객체의 ID 사용
                "password", // 더미 비밀번호
                Set.of(new SimpleGrantedAuthority("ROLE_USER")) // 실제 역할 사용
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );

        RetrieveRentRequest request = new RetrieveRentRequest("2025-3");

        when(paymentService.retrieveRent(any(RetrieveRentCommand.class)))
                .thenThrow(new BadRequestException("서비스 예외 발생"));

        // when & then
        mockMvc.perform(get("/api/v1/payment/rent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(authentication)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("공과금 조회 API 테스트")
    void retrieveUtilityTest() throws Exception {
        // given
        // Authentication 객체 생성
        UserPrincipal userPrincipal = new UserPrincipal(
                String.valueOf(user.getId()), // setUp에서 만든 user 객체의 ID 사용
                "password", // 더미 비밀번호
                Set.of(new SimpleGrantedAuthority("ROLE_USER")) // 실제 역할 사용
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );

        RetrieveUtilityRequest request = new RetrieveUtilityRequest("2025-3");

        when(paymentService.retrieveUtility(any(RetrieveUtilityCommand.class))).thenReturn(mockUtilityDTO);

        // when & then
        mockMvc.perform(get("/api/v1/payment/utility")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalAmount").value(10000))
                .andExpect(jsonPath("$.data.myAmount").value(3333))
                .andExpect(jsonPath("$.data.dueDayOfWeek").value("friday"))
                .andExpect(jsonPath("$.data.currentWeek[0].userId").value(1L))
                .andExpect(jsonPath("$.data.currentWeek[0].amount").value(3333))
                .andExpect(jsonPath("$.data.currentWeek[0].status").value(true))
                .andExpect(jsonPath("$.data.weekList[0].month").value("2025-3"))
                .andExpect(jsonPath("$.data.weekList[0].week").value(3))
                .andExpect(jsonPath("$.data.weekList[0].paidUserIds").isArray())
                .andExpect(jsonPath("$.data.weekList[0].paidUserIds[0]").value(1L))
                .andExpect(jsonPath("$.data.weekList[0].paidUserIds[1]").value(3L));
    }

    @Test
    @DisplayName("공과금 조회 서비스 예외 처리 테스트")
    void retrieveUtilityServiceExceptionTest() throws Exception {
        // given
        // Authentication 객체 생성
        UserPrincipal userPrincipal = new UserPrincipal(
                String.valueOf(user.getId()), // setUp에서 만든 user 객체의 ID 사용
                "password", // 더미 비밀번호
                Set.of(new SimpleGrantedAuthority("ROLE_USER")) // 실제 역할 사용
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );

        RetrieveUtilityRequest request = new RetrieveUtilityRequest("2025-3");

        when(paymentService.retrieveUtility(any(RetrieveUtilityCommand.class)))
                .thenThrow(new BadRequestException("서비스 예외 발생"));

        // when & then
        mockMvc.perform(get("/api/v1/payment/utility")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(authentication)))
                .andExpect(status().isBadRequest());
    }
}