package com.ssafy.chaing.fintech.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleTransferCommand {
    private String depositAccountNo;
    private String depositTransactionSummary;
    private String transactionBalance;
    private String withdrawalAccountNo;
    private String withdrawalTransactionSummary;
}
