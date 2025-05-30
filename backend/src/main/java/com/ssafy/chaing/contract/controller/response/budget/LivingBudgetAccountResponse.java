package com.ssafy.chaing.contract.controller.response.budget;

import com.ssafy.chaing.contract.service.dto.LivingBudgetAccountDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LivingBudgetAccountResponse {
    private String liveAccountNo;
    private String MyAccountNo;

    public static LivingBudgetAccountResponse from(LivingBudgetAccountDTO dto) {
        return new LivingBudgetAccountResponse(
                dto.getLiveAccountNo(),
                dto.getMyAccountNo()
        );
    }
}
