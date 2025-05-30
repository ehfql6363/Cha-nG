package com.ssafy.chaing.payment.controller.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountInfoResponse {
    private String accountNo;

    public static AccountInfoResponse from(String rentAccountNo) {
        AccountInfoResponse response = new AccountInfoResponse();
        response.accountNo = rentAccountNo;
        return response;
    }
}
