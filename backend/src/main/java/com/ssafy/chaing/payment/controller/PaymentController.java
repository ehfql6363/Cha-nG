package com.ssafy.chaing.payment.controller;

import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.common.schema.BaseResponse;
import com.ssafy.chaing.payment.controller.request.DepositTransferRequest;
import com.ssafy.chaing.payment.controller.request.RetrieveRentRequest;
import com.ssafy.chaing.payment.controller.request.RetrieveUtilityRequest;
import com.ssafy.chaing.payment.controller.request.WithdrawTransferRequest;
import com.ssafy.chaing.payment.controller.response.AccountInfoResponse;
import com.ssafy.chaing.payment.controller.response.PaymentStatusInfoResponse;
import com.ssafy.chaing.payment.controller.response.RetrieveRentResponse;
import com.ssafy.chaing.payment.controller.response.RetrieveUtilityResponse;
import com.ssafy.chaing.payment.service.PaymentService;
import com.ssafy.chaing.payment.service.command.RetrieveRentCommand;
import com.ssafy.chaing.payment.service.command.RetrieveUtilityCommand;
import com.ssafy.chaing.payment.service.command.TransferRentCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Payment API",
        description = "송금 내역 관리 API"
)
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(
            summary = "월세 통계 조회",
            description = "해당 달까지의 월세 통계 내역을 조회합니다."
    )
    @GetMapping("/rent")
    public ResponseEntity<BaseResponse<RetrieveRentResponse>> retrieveRent(
            @Valid @RequestParam RetrieveRentRequest month,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        RetrieveRentCommand command = month.toCommand(principal);
        RetrieveRentResponse response = RetrieveRentResponse.from(paymentService.retrieveRent(command));
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @Operation(
            summary = "월세, 공과금 상태 조회"
    )
    @GetMapping("/rent/current-status")
    public ResponseEntity<BaseResponse<PaymentStatusInfoResponse>> getPaymentStatus(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam int month
    ) {
        PaymentStatusInfoResponse response = paymentService.getCurrentPaymentStatus(
                principal.getId(),
                month
        );
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @Operation(
            summary = "공과금 통계 조회",
            description = "해당 달까지의 공과금 통계 내역을 조회합니다."
    )
    @GetMapping("/utility")
    public ResponseEntity<BaseResponse<RetrieveUtilityResponse>> retrieveUtility(
            @Valid @RequestParam RetrieveUtilityRequest month,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        RetrieveUtilityCommand command = month.toCommand(principal);
        RetrieveUtilityResponse response = RetrieveUtilityResponse.from(paymentService.retrieveUtility(command));
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @Operation(
            summary = "공과금 계좌 정보 조회",
            description = "공과금/월세용 계좌 정보를 조회합니다."
    )
    @GetMapping("/account")
    public ResponseEntity<BaseResponse<AccountInfoResponse>> getRentAccountNo(
            @AuthenticationPrincipal UserPrincipal principal) {
        AccountInfoResponse response = paymentService.getRentAccountNo(principal.getId());
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @Operation(
            summary = "공동 계좌 → 집주인 송금",
            description = "공동 계좌에서 집주인 계좌로 송금합니다."
    )
    @PostMapping("/withdraw")
    public ResponseEntity<BaseResponse<Void>> transferToOwner(
            @Valid @RequestBody DepositTransferRequest body,
            @AuthenticationPrincipal UserPrincipal principal) {
        TransferRentCommand transferCommand = TransferRentCommand.fromDepositRequest(body, principal.getId());
        paymentService.transferToOwner(transferCommand);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @Operation(
            summary = "멤버 → 생활비 계좌 입금",
            description = "멤버가 개인 계좌에서 공동 월세 계좌로 입금합니다."
    )
    @PostMapping("/deposit")
    public ResponseEntity<BaseResponse<Void>> depositToRentAccount(
            @Valid @RequestBody WithdrawTransferRequest body,
            @AuthenticationPrincipal UserPrincipal principal) {
        TransferRentCommand transferCommand = TransferRentCommand.fromWithdrawRequest(body, principal.getId());
        paymentService.depositToRentAccount(transferCommand);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

}
