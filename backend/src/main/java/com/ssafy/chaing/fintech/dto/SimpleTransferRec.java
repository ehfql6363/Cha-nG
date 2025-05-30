package com.ssafy.chaing.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SimpleTransferRec(
        @JsonProperty("transactionUniqueNo")
        String transactionUniqueNo,

        @JsonProperty("accountNo")
        String accountNo,

        @JsonProperty("transactionDate")
        String transactionDate,

        @JsonProperty("transactionType")
        String transactionType,

        @JsonProperty("transactionAccountNo")
        String transactionAccountNo
) {
}
