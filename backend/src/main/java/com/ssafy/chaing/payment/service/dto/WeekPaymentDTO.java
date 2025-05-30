package com.ssafy.chaing.payment.service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeekPaymentDTO {
    private String month;
    private Integer week;
    private Integer amount;
    private List<Long> paidUserIds;
    private List<Long> debtUserIds;
}
