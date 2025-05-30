package com.ssafy.chaing.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClientResponseRec(
        @JsonProperty("transactionUniqueNo")
        String transactionUniqueNo,

        @JsonProperty("accountNo")
        String accountNo,

        @JsonProperty("transactionDate")
        String transactionDate,

        @JsonProperty("transactionType")
        String transactionType,

        @JsonProperty("transactionTypeName")
        String transactionTypeName,

        @JsonProperty("transactionAccountNo")
        String transactionAccountNo
) {
}
