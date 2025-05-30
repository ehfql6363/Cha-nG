package com.ssafy.chaing.auth.controller.request;

import com.ssafy.chaing.auth.service.command.FcmCommand;

public record FcmRequest(
        String fcmToken
) {
    public FcmCommand toCommand(Long userId) {
        return new FcmCommand(userId, fcmToken);
    }

}
