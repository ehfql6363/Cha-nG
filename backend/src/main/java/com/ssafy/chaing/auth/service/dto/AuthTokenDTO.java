package com.ssafy.chaing.auth.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthTokenDTO {
    private String accessToken;
    private String refreshToken;
}
