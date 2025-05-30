package com.ssafy.chaing.notification.controller.request;

import com.ssafy.chaing.notification.service.command.ReadNotificationCommand;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadNotificationRequest {
    List<Long> notificationIds;

    public ReadNotificationCommand toCommand(Long userId) {
        return new ReadNotificationCommand(userId, notificationIds);
    }
}
