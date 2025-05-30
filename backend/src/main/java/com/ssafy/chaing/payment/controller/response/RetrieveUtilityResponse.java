package com.ssafy.chaing.payment.controller.response;

import com.ssafy.chaing.payment.service.dto.CurrentPaymentDTO;
import com.ssafy.chaing.payment.service.dto.RetrieveUtilityDTO;
import com.ssafy.chaing.payment.service.dto.WeekPaymentDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveUtilityResponse {
    private Integer totalAmount;
    private Integer myAmount;
    private String dueDayOfWeek;
    private List<CurrentPaymentDTO> currentWeek;
    private List<WeekPaymentDTO> weekList;

    public static RetrieveUtilityResponse from(RetrieveUtilityDTO dto) {
        return new RetrieveUtilityResponse(
                dto.getTotalAmount(),
                dto.getMyAmount(),
                dto.getDueDayOfWeek(),
                dto.getCurrentWeek(),
                dto.getWeekList()
        );
    }
}
