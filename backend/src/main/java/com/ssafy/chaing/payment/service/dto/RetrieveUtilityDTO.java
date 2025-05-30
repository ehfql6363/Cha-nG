package com.ssafy.chaing.payment.service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveUtilityDTO {
    private Integer totalAmount;
    private Integer myAmount;
    private String dueDayOfWeek;
    private List<CurrentPaymentDTO> currentWeek;
    private List<WeekPaymentDTO> weekList;

    public RetrieveUtilityDTO(Integer totalAmount, Integer myAmount, List<CurrentPaymentDTO> currentWeek, List<WeekPaymentDTO> weekList) {
        this.totalAmount = totalAmount;
        this.myAmount = myAmount;
        this.dueDayOfWeek = "friday";
        this.currentWeek = currentWeek;
        this.weekList = weekList;
    }
}
