package com.ssafy.chaing.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {
    private final JwtProperties jwtProperties;
}
