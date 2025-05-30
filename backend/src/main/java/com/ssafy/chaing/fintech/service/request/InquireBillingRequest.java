package com.ssafy.chaing.fintech.service.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.chaing.fintech.controller.request.InquireBillingCommand;
import com.ssafy.chaing.fintech.service.common.HeaderWithUserKeyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InquireBillingRequest {
    @JsonProperty("Header")
    private HeaderWithUserKeyDTO Header;
    private String cardNo;
    private String cvc;
    private String startMonth;
    private String endMonth;

    public InquireBillingRequest(HeaderWithUserKeyDTO header, InquireBillingCommand command) {
        this.Header = header;
        this.cardNo = command.getCardNo();
        this.cvc = command.getCvc();
        this.startMonth = command.getStartMonth();
        this.endMonth = command.getEndMonth();
    }
}
