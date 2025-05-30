package com.ssafy.chaing.payment.controller.request;

import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.payment.service.command.RetrieveRentCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveRentRequest {
    private String month;

    public RetrieveRentCommand toCommand(UserPrincipal principal) {
        String[] date = this.month.split("-");
        return new RetrieveRentCommand(
                Long.valueOf(principal.getUsername()),
                date[0],
                date[1]);
    }
}
