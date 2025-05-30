package com.ssafy.chaing.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class AuthClaims {
    private final Long userId;
}
