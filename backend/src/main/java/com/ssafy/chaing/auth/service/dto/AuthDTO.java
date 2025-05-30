package com.ssafy.chaing.auth.service.dto;

import com.ssafy.chaing.user.service.dto.UserDetailInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthDTO {
    private String accessToken;
    private UserDetailInfoDTO userInfo;
}
