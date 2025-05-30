package com.ssafy.chaing.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@ConfigurationProperties(prefix = "firebase")
@Getter
public class FirebaseProperties {
    private String serviceAccountBase64;
    private String projectName;

}
