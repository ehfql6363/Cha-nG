package com.ssafy.chaing.auth.service.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FcmCommand {
    final Long userId;
    final String fcmToken;
}
