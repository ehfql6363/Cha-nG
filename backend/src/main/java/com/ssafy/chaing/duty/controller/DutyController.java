package com.ssafy.chaing.duty.controller;

import com.ssafy.chaing.common.schema.BaseResponse;
import com.ssafy.chaing.duty.controller.request.DutyFormRequest;
import com.ssafy.chaing.duty.controller.response.DutyDetailResponse;
import com.ssafy.chaing.duty.controller.response.DutyListResponse;
import com.ssafy.chaing.duty.controller.response.RemovedDutyResponse;
import com.ssafy.chaing.duty.service.DutyService;
import com.ssafy.chaing.recommend.Request.RecommendRequest;
import com.ssafy.chaing.recommend.response.RecommendResponse;
import com.ssafy.chaing.recommend.service.RecommendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Duty API",
        description = "당번 관리 API"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/duty")
public class DutyController {

    private final DutyService dutyService;
    private final RecommendService recommendService;

    @Operation(
            summary = "당번 리스트 조회",
            description = "그룹 ID에 해당하는 전체 당번 정보를 요일별로 조회합니다."
    )
    @GetMapping("/{groupId}")
    public ResponseEntity<BaseResponse<DutyListResponse>> getDuties(@PathVariable("groupId") Long groupId) {
        DutyListResponse duties = dutyService.getDuties(groupId);
        return ResponseEntity.ok(BaseResponse.success(duties));
    }

    @Operation(
            summary = "당번 생성",
            description = "특정 그룹에 새로운 당번을 생성합니다.")

    @PostMapping("/{groupId}")
    public ResponseEntity<BaseResponse<DutyDetailResponse>> createDuty(
            @PathVariable("groupId") Long groupId,
            @RequestBody DutyFormRequest body) {
        DutyDetailResponse dutyDetailResponse = dutyService.creatDuty(groupId, body);
        return ResponseEntity.ok(BaseResponse.success(dutyDetailResponse));

    }

    @Operation(
            summary = "당번 수정",
            description = "기존 당번 정보를 수정합니다."
    )
    @PatchMapping("/{dutyId}")
    public ResponseEntity<BaseResponse<DutyDetailResponse>> modifyDuty(
            @PathVariable("dutyId") Long dutyId,
            @RequestBody DutyFormRequest body) {

        DutyDetailResponse dutyDetailResponse = dutyService.updateDuty(dutyId, body);
        return ResponseEntity.ok(BaseResponse.success(dutyDetailResponse));
    }

    @Operation(
            summary = "당번 삭제",
            description = "특정 당번을 삭제합니다."
    )
    @DeleteMapping("/{dutyId}")
    public ResponseEntity<BaseResponse<RemovedDutyResponse>> deleteDuty(
            @PathVariable("dutyId") Long dutyId) {
        RemovedDutyResponse removedDutyResponse = dutyService.removeDuty(dutyId);
        return ResponseEntity.ok(BaseResponse.success(removedDutyResponse));
    }

    @Operation(
            summary = "카테고리 추천",
            description = "당번 카테고리를 추천합니다."
    )
    @PostMapping("/category")
    public ResponseEntity<BaseResponse<RecommendResponse>> recommendCategory(
            @RequestBody RecommendRequest body) {
        RecommendResponse response = recommendService.recommendDutyCategory(body);
        return ResponseEntity.ok(BaseResponse.success(response));
    }
}
