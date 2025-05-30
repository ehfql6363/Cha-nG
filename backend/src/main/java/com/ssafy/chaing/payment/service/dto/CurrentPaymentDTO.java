package com.ssafy.chaing.payment.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentPaymentDTO {
    private Long userId;
    private Integer amount;
    private Boolean status;
}
