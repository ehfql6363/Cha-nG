package com.ssafy.chaing.contract.controller;

import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.common.schema.BaseResponse;
import com.ssafy.chaing.contract.controller.request.CreateLivingBudgetRequest;
import com.ssafy.chaing.contract.controller.response.budget.LivingBudgetAccountResponse;
import com.ssafy.chaing.contract.service.BudgetService;
import com.ssafy.chaing.contract.service.dto.CreateLivingBudgetDto;
import com.ssafy.chaing.contract.service.dto.LivingBudgetAccountDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Budget API",
        description = "생활비 계좌 관련 API"
)
@RestController
@RequestMapping("/api/v1/budget/living")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @Operation(
            summary = "그룹장에게 계좌 등록 요청 알림 전송",
            description = "생활비 계좌 등록이 필요함을 그룹장에게 알림으로 전송합니다."
    )
    @GetMapping("/notice/create")
    public ResponseEntity<BaseResponse<Void>> notifyLeaderLivingAccountCreated(
            @AuthenticationPrincipal UserPrincipal principal) {
        budgetService.notifyLeaderToRegisterLivingAccount(principal.getId());
        return ResponseEntity.ok(BaseResponse.success(null));
    }


    @Operation(
            summary = "생활비 계좌 및 개인 계좌 조회",
            description = "현재 로그인한 사용자의 생활비 계좌 정보를 조회합니다."
    )
    @GetMapping("/account")
    public ResponseEntity<BaseResponse<LivingBudgetAccountResponse>> getLivingAccount(
            @AuthenticationPrincipal UserPrincipal principal) {

        LivingBudgetAccountDTO dto = budgetService.getLivingAccount(
                principal.getId()
        );
        
        LivingBudgetAccountResponse response = LivingBudgetAccountResponse.from(dto);

        return ResponseEntity.ok(
                BaseResponse.success(response)
        );
    }

    @Operation(
            summary = "생활비 계좌 저장 및 알림 전송",
            description = "생활비 계좌를 저장하고 관련 알림을 그룹원에게 전송합니다."
    )
    @PostMapping("/account")
    public ResponseEntity<BaseResponse<Void>> saveAccountAndNotify(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody CreateLivingBudgetRequest body) {

        CreateLivingBudgetDto dto = new CreateLivingBudgetDto(principal.getId(), body.getAccountNo());
        budgetService.saveAccountAndNotify(dto);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @Operation(
            summary = "입금 알림 트리거",
            description = "입금 상황에 대한 알림을 트리거합니다. 실제 입금 이벤트 처리와 연결 필요"
    )
    @GetMapping("/notice/deposit")
    public ResponseEntity<BaseResponse<Void>> notifyLivingDeposit(
            @AuthenticationPrincipal UserPrincipal principal) {

        budgetService.notifyLivingDeposit(principal.getId());
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @Operation(
            summary = "출금 알림 트리거",
            description = "출금 상황에 대한 알림을 트리거합니다. 실제 출금 이벤트 처리와 연결 필요"
    )
    @PostMapping("/notice/withdraw")
    public ResponseEntity<BaseResponse<Void>> notifyLivingWithdraw(
            @AuthenticationPrincipal UserPrincipal principal) {
        budgetService.notifyLivingWithdraw(principal.getId());
        return ResponseEntity.ok(BaseResponse.success(null));
    }

}
