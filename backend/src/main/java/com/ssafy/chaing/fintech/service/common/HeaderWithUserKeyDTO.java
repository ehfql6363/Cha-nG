package com.ssafy.chaing.fintech.service.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class HeaderWithUserKeyDTO {
    private final String apiName;
    private final String transmissionDate;
    private final String transmissionTime;
    private final String institutionCode;
    private final String fintechAppNo;
    private final String apiServiceCode;
    private final String institutionTransactionUniqueNo;
    private final String apiKey;
    private final String userKey;
}

