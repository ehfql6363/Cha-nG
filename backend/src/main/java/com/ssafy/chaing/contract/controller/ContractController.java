package com.ssafy.chaing.contract.controller;

import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.common.schema.BaseResponse;
import com.ssafy.chaing.contract.controller.request.ApproveContractRequest;
import com.ssafy.chaing.contract.controller.request.ConfirmContractRequest;
import com.ssafy.chaing.contract.controller.request.EmptyContractRequest;
import com.ssafy.chaing.contract.controller.request.UpdateDraftContractRequest;
import com.ssafy.chaing.contract.controller.response.ContractDetailResponse;
import com.ssafy.chaing.contract.controller.response.ContractMemberResponse;
import com.ssafy.chaing.contract.controller.response.DraftContractResponse;
import com.ssafy.chaing.contract.service.ContractService;
import com.ssafy.chaing.contract.service.dto.ContractDTO;
import com.ssafy.chaing.contract.service.dto.ContractDetailDTO;
import com.ssafy.chaing.contract.service.dto.ContractUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Group Controller",
        description = "계약 정보 관리"
)
@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/contract")
public class ContractController {

    private final ContractService contractService;

    @Operation(
            summary = "계약 상세 조회",
            description = "특정 계약 ID에 대한 상세 정보를 조회합니다."
    )
    @GetMapping("/{contractId}")
    public ResponseEntity<BaseResponse<ContractDetailResponse>> getContract(@PathVariable Long contractId) {
        ContractDetailDTO contract = contractService.getContract(contractId);
        ContractDetailResponse response = ContractDetailResponse.fromDTO(contract);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @Operation(
            summary = "빈 계약서 생성",
            description = "그룹 ID를 기반으로 초기화된 초안 계약서를 생성합니다. 최초 계약 시작 시 사용합니다."
    )
    @PostMapping
    public ResponseEntity<BaseResponse<DraftContractResponse>> createEmptyContract(
            @RequestBody EmptyContractRequest body,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        ContractDTO contractDTO = contractService.createDraftContract(body.groupId(), principal.getId());
        DraftContractResponse response = DraftContractResponse.from(contractDTO);
        return ResponseEntity.created(URI.create("/contract/" + contractDTO.getId()))
                .body(BaseResponse.success(response));

    }

    @Operation(
            summary = "초안 계약 수정",
            description = "작성 중인 초안 계약서를 수정합니다. 본인이 작성자여야 하며, 계약이 확정되기 전까지만 수정 가능합니다."
    )
    @PutMapping("/{contractId}")
    public ResponseEntity<BaseResponse<ContractDetailResponse>> updateContract(
            @PathVariable Long contractId,
            @RequestBody UpdateDraftContractRequest body,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        ContractDetailDTO contractDTO = contractService.updateContract(
                contractId,
                body.toCommand(principal.getId())
        );

        return ResponseEntity.ok(
                BaseResponse.success(
                        ContractDetailResponse.fromDTO(contractDTO)
                )
        );
    }

    @Operation(
            summary = "계약 확정 요청",
            description = "계약을 작성 완료 상태로 전환하여 승인 요청 상태로 만듭니다. 이후 참여자들의 승인이 필요합니다."
    )
    @PutMapping("/{contractId}/pending")
    public ResponseEntity<BaseResponse<ContractDetailResponse>> confirmContract(
            @PathVariable Long contractId,
            @RequestBody ConfirmContractRequest body,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        // 계약을 확정하는 서비스 메서드 호출
        ContractDetailDTO contractDTO = contractService.confirmContract(
                contractId,
                body.toCommand(principal.getId())
        );

        return ResponseEntity.ok(
                BaseResponse.success(
                        ContractDetailResponse.fromDTO(contractDTO)
                )
        );
    }

    @Operation(
            summary = "계약 승인",
            description = "참여자가 계약에 대한 승인 의사를 표시합니다. 모든 참여자가 승인 시 계약이 확정됩니다."
    )
    @PostMapping("/{contractId}/approve")
    public ResponseEntity<BaseResponse<Void>> approveContract(
            @PathVariable Long contractId,
            @RequestBody ApproveContractRequest body,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        contractService.approveContract(
                contractId,
                body.toCommand(principal.getId())
        );

        // 응답으로 변환
        return ResponseEntity.ok(
                BaseResponse.success(
                        null
                )
        );
    }

    @Operation(
            summary = "계약 참여자 목록 조회",
            description = "계약서에 참여하고 있는 모든 구성원 정보를 조회합니다. 승인 여부와 역할도 함께 제공됩니다."
    )
    @GetMapping("/{contractId}/members")
    public ResponseEntity<BaseResponse<List<ContractMemberResponse>>> getContractMembers(
            @PathVariable Long contractId
    ) {
        List<ContractUserDTO> dto = contractService.getContractMembers(contractId);
        List<ContractMemberResponse> response = dto.stream()
                .map(ContractMemberResponse::from).toList();

        return ResponseEntity.ok(
                BaseResponse.success(response)
        );
    }


}
