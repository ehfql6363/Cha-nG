package com.ssafy.chaing.fintech.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class SsafyApiConfig {

    @Value("${ssafy.fintech.card-unique-no}")
    private String cardUniqueNo;

    @Value("${ssafy.fintech.base-url}")
    private String baseUrl;

}
