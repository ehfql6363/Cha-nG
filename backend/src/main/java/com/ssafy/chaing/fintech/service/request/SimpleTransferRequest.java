package com.ssafy.chaing.fintech.service.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.chaing.fintech.controller.request.SimpleTransferCommand;
import com.ssafy.chaing.fintech.service.common.HeaderWithUserKeyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleTransferRequest {
    @JsonProperty("Header")
    private HeaderWithUserKeyDTO header;

    @JsonProperty("depositAccountNo")
    private String depositAccountNo;

    @JsonProperty("depositTransactionSummary")
    private String depositTransactionSummary;

    @JsonProperty("transactionBalance")
    private String transactionBalance;

    @JsonProperty("withdrawalAccountNo")
    private String withdrawalAccountNo;

    @JsonProperty("withdrawalTransactionSummary")
    private String withdrawalTransactionSummary;

    public SimpleTransferRequest(HeaderWithUserKeyDTO header, SimpleTransferCommand command) {
        this.header = header;
        this.depositAccountNo = command.getDepositAccountNo();
        this.depositTransactionSummary = command.getDepositTransactionSummary();
        this.transactionBalance = command.getTransactionBalance();
        this.withdrawalAccountNo = command.getWithdrawalAccountNo();
        this.withdrawalTransactionSummary = command.getWithdrawalTransactionSummary();
    }
}
