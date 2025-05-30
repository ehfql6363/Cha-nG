package com.ssafy.chaing.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record InquireTransactionHistoryRec(
        @JsonProperty("totalCount")
        String totalCount,

        @JsonProperty("list")
        List<History> list
) {
    public record History(
            @JsonProperty("transactionUniqueNo")
            String transactionUniqueNo,

            @JsonProperty("transactionDate")
            String transactionDate,

            @JsonProperty("transactionTime")
            String transactionTime,

            @JsonProperty("transactionType")
            String transactionType,

            @JsonProperty("transactionTypeName")
            String transactionTypeName,

            @JsonProperty("transactionAccountNo")
            String transactionAccountNo,

            @JsonProperty("transactionBalance")
            String transactionBalance,

            @JsonProperty("transactionAfterBalance")
            String transactionAfterBalance,

            @JsonProperty("transactionSummary")
            String transactionSummary,

            @JsonProperty("transactionMemo")
            String transactionMemo
    ){}
}
