package com.ssafy.chaing.user.service.command;

import com.ssafy.chaing.user.controller.request.UpdateUserProfileRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateUserProfileCommand {
    private Long userId;
    private String nickname;
    private String profileImage;

    public static UpdateUserProfileCommand from(Long userId, UpdateUserProfileRequest body) {
        return new UpdateUserProfileCommand(
                userId,
                body.getNickname(),
                body.getProfileImage()
        );
    }
}
