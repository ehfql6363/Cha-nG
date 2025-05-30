package com.ssafy.chaing.payment.service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveRentDTO {
    private Integer totalAmount;
    private Integer myAmount;
    private Integer dueDate;
    private List<CurrentPaymentDTO> currentMonth;
    private List<MonthPaymentDTO> monthList;
}
