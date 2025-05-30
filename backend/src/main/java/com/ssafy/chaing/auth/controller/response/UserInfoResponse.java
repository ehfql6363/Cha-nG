package com.ssafy.chaing.auth.controller.response;

import com.ssafy.chaing.user.service.dto.UserDetailInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String name;
    private String nickname;
    private String profileImage;
    private Long groupId;
    private Long contractId;

    public static UserInfoResponse from(UserDetailInfoDTO dto) {
        return new UserInfoResponse(
                dto.getId(),
                dto.getName(),
                dto.getNickname(),
                dto.getProfileImage(),
                dto.getGroupId(),
                dto.getContractId()
        );
    }
}
