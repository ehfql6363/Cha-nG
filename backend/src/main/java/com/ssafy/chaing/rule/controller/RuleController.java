package com.ssafy.chaing.rule.controller;


import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.common.schema.BaseResponse;
import com.ssafy.chaing.recommend.Request.RecommendRequest;
import com.ssafy.chaing.recommend.response.RecommendResponse;
import com.ssafy.chaing.recommend.service.RecommendService;
import com.ssafy.chaing.rule.controller.request.LifeRuleApproveRequest;
import com.ssafy.chaing.rule.controller.request.LifeRuleFormRequest;
import com.ssafy.chaing.rule.controller.request.LifeRuleUpdateRequest;
import com.ssafy.chaing.rule.controller.response.LifeRuleResponse;
import com.ssafy.chaing.rule.controller.response.NotApproveUserResponse;
import com.ssafy.chaing.rule.dto.LifeRuleUpdateDto;
import com.ssafy.chaing.rule.service.RuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(
        name = "Rule Controller",
        description = "생활 룰 관리"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/life-rule")
public class RuleController {

    private final RuleService ruleService;
    private final RecommendService recommendService;

    @Operation(
            summary = "생활 룰 생성",
            description = "그룹의 생활 룰을 생성합니다. 최초 1회만 가능하며 룰 항목을 함께 등록합니다."
    )
    @PostMapping
    public ResponseEntity<BaseResponse<LifeRuleResponse>> createLifeRule(
            @RequestBody LifeRuleFormRequest body,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        LifeRuleResponse lifeRule = ruleService.createLifeRule(body, principal.getId());
        return ResponseEntity.ok(BaseResponse.success(lifeRule));
    }

    @Operation(
            summary = "생활 룰 조회",
            description = "현재 사용자가 속한 그룹의 생활 룰을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<BaseResponse<LifeRuleResponse>> getLifeRule(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        LifeRuleResponse lifeRules = ruleService.getLifeRules(principal.getId());
        return ResponseEntity.ok(BaseResponse.success(lifeRules));
    }

    @Operation(
            summary = "생활 룰 변경 요청",
            description = "생활 룰 항목에 대한 변경 요청을 보냅니다. 생성/수정/삭제 액션 포함"
    )
    @PostMapping("/update")
    public ResponseEntity<BaseResponse<List<LifeRuleUpdateDto>>> updateLifeRule(
            @RequestBody LifeRuleUpdateRequest body,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        List<LifeRuleUpdateDto> lifeRuleUpdates = ruleService.updateRules(body, principal.getId());
        return ResponseEntity.ok(BaseResponse.success(lifeRuleUpdates));
    }

    @Operation(
            summary = "변경 요청된 룰 목록 조회",
            description = "변경 요청 상태의 룰 항목들을 조회합니다. 승인/거절 전 상태 기준"
    )
    @GetMapping("/update")
    public ResponseEntity<BaseResponse<List<LifeRuleUpdateDto>>> getUpdateLifeRule(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        List<LifeRuleUpdateDto> updateLifeRule = ruleService.getUpdateLifeRule(principal.getId());
        return ResponseEntity.ok(BaseResponse.success(updateLifeRule));
    }

    @Operation(
            summary = "생활 룰 변경 승인 또는 거절",
            description = "변경 요청된 룰에 대해 승인하거나 거절 처리합니다."
    )
    @PostMapping("/approve")
    public ResponseEntity<BaseResponse<Void>> approveUpdateForm(
            @RequestBody LifeRuleApproveRequest body,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        ruleService.approveLifeRule(body, principal.getId());
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    @Operation(
            summary = "생활 룰 카테고리 추천",
            description = "AI 기반으로 적합한 생활 룰 카테고리를 추천합니다."
    )
    @PostMapping("/category")
    public ResponseEntity<BaseResponse<RecommendResponse>> recommendCategory(
            @RequestBody RecommendRequest body
    ) {
        RecommendResponse response = recommendService.recommendLifeRuleCategory(body);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @Operation(
            summary = "생활룰 승인 안한 명단 조회",
            description = "생활룰 변경 사항에 대한 승인 안한 명단 조회"
    )
    @PostMapping("/not-approved/{groupId}")
    public ResponseEntity<BaseResponse<NotApproveUserResponse>> recommendCategory(
            @PathVariable("groupId") Long groupId
    ) {
        NotApproveUserResponse response = ruleService.getApprovedUserList(groupId);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

}
