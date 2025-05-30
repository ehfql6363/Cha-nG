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
public class MonthPaymentDTO {
    private String month;
    private List<Long> paidUserIds;
    private List<Long> debtUserIds;

}
