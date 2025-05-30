package com.ssafy.chaing.common.config;


import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(FirebaseProperties.class)
public class FirebasePropertiesConfig {
}
