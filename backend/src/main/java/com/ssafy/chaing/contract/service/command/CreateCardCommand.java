package com.ssafy.chaing.contract.service.command;

public record CreateCardCommand(
        String accountNo,
        Long userId
) {
}
