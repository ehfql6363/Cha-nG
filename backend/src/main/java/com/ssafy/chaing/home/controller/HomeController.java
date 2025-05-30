package com.ssafy.chaing.home.controller;

import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.common.schema.BaseResponse;
import com.ssafy.chaing.contract.service.dto.ContractDTO;
import com.ssafy.chaing.home.controller.request.TestIntegrationService;
import com.ssafy.chaing.home.controller.request.TestPaymentRequest;
import com.ssafy.chaing.home.controller.response.HomeOverviewResponse;
import com.ssafy.chaing.home.service.HomeFacade;
import com.ssafy.chaing.home.service.command.HomeOverviewCommand;
import com.ssafy.chaing.home.service.dto.HomeOverviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/v1/home")
@RestController
public class HomeController {

    private final HomeFacade homeFacade;
    private final TestIntegrationService integrationService;

    @GetMapping
    public ResponseEntity<BaseResponse<HomeOverviewResponse>> getHomeOverview(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        HomeOverviewDTO dto = homeFacade.getHomeOverview(new HomeOverviewCommand(principal.getId()));
        HomeOverviewResponse response = HomeOverviewResponse.from(dto);

        return ResponseEntity.ok()
                .body(BaseResponse.success(response));
    }

    @PostMapping("/test-payment")
    @Operation(
            summary = "테스트용 계약 생성"
    )
    public ResponseEntity<BaseResponse<ContractDTO>> createTestPayment(
            @RequestBody TestPaymentRequest body

    ) {
        ContractDTO contractDTO = integrationService.createTestPayment(
                body
        );

        return ResponseEntity.ok()
                .body(BaseResponse.success(contractDTO));
    }

}
