package com.ssafy.chaing.payment.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawTransferRequest {
    private int month;
    private String withdrawalAccountNo;
    private int transactionBalance;
}
