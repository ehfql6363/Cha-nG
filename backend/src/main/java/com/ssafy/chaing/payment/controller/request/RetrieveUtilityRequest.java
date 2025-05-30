package com.ssafy.chaing.payment.controller.request;

import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.payment.service.command.RetrieveRentCommand;
import com.ssafy.chaing.payment.service.command.RetrieveUtilityCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveUtilityRequest {
    private String month;

    public RetrieveUtilityCommand toCommand(UserPrincipal principal) {
        String[] date = this.month.split("-");
        return new RetrieveUtilityCommand(
                Long.valueOf(principal.getUsername()),
                date[0],
                date[1]);
    }
}
