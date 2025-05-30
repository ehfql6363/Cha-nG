package com.ssafy.chaing.user.service.dto;


import com.ssafy.chaing.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String emailAddress;
    private String name;
    private String nickname;
    private String profileImage;

    static public UserDTO fromEntity(UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getId())
                .emailAddress(userEntity.getEmailAddress())
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .profileImage(userEntity.getProfileImage())
                .build();
    }
}
