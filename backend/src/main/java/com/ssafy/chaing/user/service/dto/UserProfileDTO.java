package com.ssafy.chaing.user.service.dto;

import com.ssafy.chaing.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String emailAddress;
    private String name;
    private String nickname;
    private String profileImage;
    private String myAccountNo;
    private String ownerAccountNo;

    public static UserProfileDTO from(UserEntity user) {
        return new UserProfileDTO(
                user.getId(),
                user.getEmailAddress(),
                user.getName(),
                user.getNickname(),
                user.getProfileImage(),
                null,
                null
        );
    }

    public static UserProfileDTO from(UserEntity user, String myAccountNo, String ownerAccountNo) {
        return new UserProfileDTO(
                user.getId(),
                user.getEmailAddress(),
                user.getName(),
                user.getNickname(),
                user.getProfileImage(),
                myAccountNo,
                ownerAccountNo
        );
    }
}
