package com.ssafy.chaing.group.controller.response;

import com.ssafy.chaing.group.service.dto.GroupUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class GroupUserResponse {
    private Long id;
    private String name;
    private String nickname;
    private String profileImage;

    public static GroupUserResponse from(GroupUserDTO user) {
        return new GroupUserResponse(
                user.getId(),
                user.getName(),
                user.getNickname(),
                user.getProfileImage()
        );
    }
}
