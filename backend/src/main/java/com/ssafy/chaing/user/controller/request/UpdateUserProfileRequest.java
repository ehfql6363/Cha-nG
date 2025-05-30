package com.ssafy.chaing.user.controller.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserProfileRequest {
    private String nickname;
    private String profileImage;
}
