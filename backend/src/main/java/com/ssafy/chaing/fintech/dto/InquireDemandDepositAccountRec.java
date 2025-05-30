package com.ssafy.chaing.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InquireDemandDepositAccountRec(
        @JsonProperty("bankCode")
        String bankCode,

        @JsonProperty("bankName")
        String bankName,

        @JsonProperty("userName")
        String userName,

        @JsonProperty("accountNo")
        String accountNo,

        @JsonProperty("accountName")
        String accountName,

        @JsonProperty("accountTypeCode")
        String accountTypeCode,

        @JsonProperty("accountTypeName")
        String accountTypeName,

        @JsonProperty("accountCreatedDate")
        String accountCreatedDate,

        @JsonProperty("accountExpiryDate")
        String accountExpiryDate,

        @JsonProperty("dailyTransferLimit")
        String dailyTransferLimit,

        @JsonProperty("oneTimeTransferLimit")
        String oneTimeTransferLimit,

        @JsonProperty("accountBalance")
        String accountBalance,

        @JsonProperty("lastTransactionDate")
        String lastTransactionDate,

        @JsonProperty("currency")
        String currency
) {
}
