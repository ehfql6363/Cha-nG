package com.ssafy.chaing.payment.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class PaymentOverviewDTO {
    private String groupName;
    private Boolean rentPaymentStatus;
    private Boolean userRentPaymentStatus;
    private Boolean utilityPaymentStatus;
    private Boolean userUtilityPaymentStatus;
}
