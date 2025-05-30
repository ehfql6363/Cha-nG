package com.ssafy.chaing.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateAccountRec(
        @JsonProperty("bankCode")
        String bankCode,

        @JsonProperty("accountNo")
        String accountNo,

        @JsonProperty("currency")
        Currency currency
) {
    public record Currency(
            @JsonProperty("currency")
            String currency,

            @JsonProperty("currencyName")
            String currencyName
    ) {
    }
}
