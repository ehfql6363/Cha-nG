package com.ssafy.chaing.payment.controller.response;

import com.ssafy.chaing.payment.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PaymentStatusInfoResponse {
    private PaymentStatus rent;
    private PaymentStatus userRent;
    private PaymentStatus utility;
    private PaymentStatus userUtility;
}
