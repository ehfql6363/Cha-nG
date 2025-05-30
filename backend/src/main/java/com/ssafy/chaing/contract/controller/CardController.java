package com.ssafy.chaing.contract.controller;

import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.common.schema.BaseResponse;
import com.ssafy.chaing.contract.controller.request.CreateCardRequest;
import com.ssafy.chaing.contract.controller.response.CreateCardResponse;
import com.ssafy.chaing.contract.service.CardService;
import com.ssafy.chaing.contract.service.command.CreateCardCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Card API", description = "공과금 카드 관리 API")
@RestController
@RequestMapping("/api/v1/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    @Operation(
            summary = "공과금 카드 등록",
            description = "생활비 카드 또는 공과금 결제를 위한 카드를 등록합니다."
    )
    @PostMapping
    public ResponseEntity<BaseResponse<CreateCardResponse>> createCard(
            @RequestBody CreateCardRequest body,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        CreateCardCommand command = body.toCommand(principal.getId());

        CreateCardResponse response = CreateCardResponse.from(cardService.registerUtilityCard(command));
        return ResponseEntity.ok(
                BaseResponse.success(
                        response
                )
        );
    }
}
