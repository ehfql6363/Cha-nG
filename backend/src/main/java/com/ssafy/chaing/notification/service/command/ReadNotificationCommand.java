package com.ssafy.chaing.notification.service.command;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadNotificationCommand {
    private Long userId;
    private List<Long> notificationIds;
}
