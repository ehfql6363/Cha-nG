package com.ssafy.chaing.fintech.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountHistoryCommand {
    private String accountNo;
    private String startDate;
    private String endDate;
    private String transactionType;
    private String orderByType;
}
