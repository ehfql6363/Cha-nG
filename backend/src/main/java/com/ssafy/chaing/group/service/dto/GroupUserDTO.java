package com.ssafy.chaing.group.service.dto;

import com.ssafy.chaing.group.domain.GroupUserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupUserDTO {
    private Long id;
    private String name;
    private String nickname;
    private String profileImage;

    public static GroupUserDTO from(GroupUserEntity entity) {
        return new GroupUserDTO(
                entity.getUser().getId() ,
                entity.getUser().getName(),
                entity.getUser().getNickname(),
                entity.getUser().getProfileImage()
        );
    }
}
