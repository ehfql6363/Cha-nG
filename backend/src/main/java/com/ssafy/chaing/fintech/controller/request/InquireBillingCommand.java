package com.ssafy.chaing.fintech.controller.request;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InquireBillingCommand {
    private String cardNo;
    private String cvc;
    private String startMonth;
    private String endMonth;

    public InquireBillingCommand(String cardNo, String cvc) {
        this.cardNo = cardNo;
        this.cvc = cvc;
        setBillingPeriod();
    }

    public void setBillingPeriod() {
        ZonedDateTime today = ZonedDateTime.now(ZoneId.of("UTC"));
        this.endMonth = today.format(DateTimeFormatter.ofPattern("yyyyMM"));
        this.startMonth = today.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
    }
    
}
