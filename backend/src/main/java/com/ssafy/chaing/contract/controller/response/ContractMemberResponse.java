package com.ssafy.chaing.contract.controller.response;

import com.ssafy.chaing.contract.service.dto.ContractUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ContractMemberResponse {
    private Long id;
    private String name;
    private String nickname;
    private String profileImage;
    private boolean approved;  // 사용자가 계약을 승인했는지 여부
    private String status;  // 사용자의 계약 상태 (예: WRITING, PENDING, CONFIRMED, REVIEW_REQUIRED)

    public static ContractMemberResponse from(ContractUserDTO dto) {
        return new ContractMemberResponse(
                dto.getId(),
                dto.getName(),
                dto.getNickname(),
                dto.getProfileImage(),
                dto.isApproved(),
                dto.getStatus().name()
        );
    }
}
