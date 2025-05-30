package com.ssafy.chaing.contract.controller.request;

import com.ssafy.chaing.contract.service.command.CreateCardCommand;

public record CreateCardRequest(
        String accountNo
) {
    public CreateCardCommand toCommand(Long userId) { // del static
        return new CreateCardCommand(accountNo, userId);
    }
}
