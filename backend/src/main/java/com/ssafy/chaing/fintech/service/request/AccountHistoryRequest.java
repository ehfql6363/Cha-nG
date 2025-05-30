package com.ssafy.chaing.fintech.service.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.chaing.fintech.controller.request.AccountHistoryCommand;
import com.ssafy.chaing.fintech.service.common.HeaderWithUserKeyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountHistoryRequest {
    @JsonProperty("Header")
    private HeaderWithUserKeyDTO header;

    @JsonProperty("accountNo")
    private String accountNo;

    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("endDate")
    private String endDate;

    @JsonProperty("transactionType")
    private String transactionType;

    @JsonProperty("orderByType")
    private String orderByType;

    public AccountHistoryRequest(HeaderWithUserKeyDTO header, AccountHistoryCommand command) {
        this.header = header;
        this.accountNo = command.getAccountNo();
        this.startDate = command.getStartDate();
        this.endDate = command.getEndDate();
        this.transactionType = command.getTransactionType();
        this.orderByType = command.getOrderByType();
    }
}
