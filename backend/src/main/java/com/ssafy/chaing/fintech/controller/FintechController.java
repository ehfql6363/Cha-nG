package com.ssafy.chaing.fintech.controller;

import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.common.schema.BaseResponse;
import com.ssafy.chaing.fintech.controller.request.AccountHistoryCommand;
import com.ssafy.chaing.fintech.controller.request.ManualTransferCommand;
import com.ssafy.chaing.fintech.controller.request.SimpleTransferCommand;
import com.ssafy.chaing.fintech.controller.response.FintechResponse;
import com.ssafy.chaing.fintech.dto.CreateAccountRec;
import com.ssafy.chaing.fintech.dto.InquireDemandDepositAccountRec;
import com.ssafy.chaing.fintech.dto.InquireTransactionHistoryRec;
import com.ssafy.chaing.fintech.dto.SimpleTransferRec;
import com.ssafy.chaing.fintech.service.FintechService;
import com.ssafy.chaing.fintech.service.dto.TransferDTO;
import com.ssafy.chaing.fintech.service.response.ClientErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Fintech API", description = "핀테크 송금 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/fintech")
@RestController
public class FintechController {

    private final FintechService fintechService;

    @Operation(
            summary = "생활비 송금",
            description = "공동생활비 또는 정산을 위한 송금 요청을 처리합니다."
    )
    @PostMapping("/transfer")
    public ResponseEntity<BaseResponse<TransferDTO>> transfer(
            @RequestBody ManualTransferCommand body,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        TransferDTO dto = fintechService.manualTransfer(body, principal.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(dto));
    }

    @Operation(summary = "계좌 상세 조회", description = "계좌 번호를 이용하여 상세 정보를 조회합니다.") // API 설명 추가
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            // 성공 시 응답 구조 명시
                            schema = @Schema(implementation = InquireDemandDepositAccountRec.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패 또는 잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            // 실패 시 응답 구조 명시
                            schema = @Schema(implementation = ClientErrorResponse.class)))
            // 필요한 다른 응답 코드 (e.g., 404 Not Found, 500 Internal Server Error) 도 추가 가능
    })
    @GetMapping("/account/{accountNo}")
    public ResponseEntity<BaseResponse<FintechResponse<?>>> getAccountDetail(
            @PathVariable String accountNo
    ) {
        FintechResponse<?> response = fintechService.inquireDemandDepositAccount(accountNo);

        if (response.getData() instanceof InquireDemandDepositAccountRec) {
            return ResponseEntity.ok(BaseResponse.success(response));
        }
        return ResponseEntity.badRequest().body(BaseResponse.error(response));
    }

    @Operation(summary = "계좌 생성", description = "핀테크 API를 사용해서 계좌를 생성합니다.") // API 설명 추가
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공",
                    content = @Content(mediaType = "application/json",
                            // 성공 시 응답 구조 명시
                            schema = @Schema(implementation = CreateAccountRec.class))),
            @ApiResponse(responseCode = "400", description = "조회 실패 또는 잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            // 실패 시 응답 구조 명시
                            schema = @Schema(implementation = ClientErrorResponse.class)))
            // 필요한 다른 응답 코드 (e.g., 404 Not Found, 500 Internal Server Error) 도 추가 가능
    })
    @PostMapping("/account")
    public ResponseEntity<?> createAccount() {
        FintechResponse<?> response = fintechService.createAccount();
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @Operation(
            summary = "계좌 거래 내역 조회",
            description = "거래 내역을 조회하는 API 입니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "거래 내역 조회 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InquireTransactionHistoryRec.class))),
                    @ApiResponse(responseCode = "400",
                            description = "거래 내역 조회 실패",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClientErrorResponse.class)))
            }
    )
    @PostMapping("/account/history")
    public ResponseEntity<BaseResponse<FintechResponse<?>>> getAccountHistory(
            @Valid @RequestBody AccountHistoryCommand body
    ) {
        FintechResponse<?> response = fintechService.getAccountHistory(body);
        if (response.getData() instanceof InquireTransactionHistoryRec) {
            return ResponseEntity.ok(BaseResponse.success(response));
        }
        return ResponseEntity.badRequest().body(BaseResponse.error(response));
    }

    @Operation(
            summary = "단순 계좌 이체",
            description = "Fintech API로 단순 계좌 이체하는 API입니다."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "계좌 이체 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = List.class))),
                    @ApiResponse(responseCode = "400",
                            description = "거래 내역 조회 실패",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClientErrorResponse.class)))
            }
    )
    @PostMapping("/simple/transfer")
    public ResponseEntity<BaseResponse<FintechResponse<?>>> transferWithSimple(
            @Valid @RequestBody SimpleTransferCommand body
    ) {
        FintechResponse<?> response = fintechService.transferWithSimple(body);
        if (response.getData() instanceof List) {
            return ResponseEntity.ok(BaseResponse.success(response));
        }
        return ResponseEntity.badRequest().body(BaseResponse.error(response));
    }
}
