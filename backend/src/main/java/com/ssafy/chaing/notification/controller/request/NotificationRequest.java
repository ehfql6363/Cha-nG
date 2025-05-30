package com.ssafy.chaing.notification.controller.request;

import com.ssafy.chaing.notification.domain.NotificationCategory;
import com.ssafy.chaing.notification.service.command.NotificationCommand;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class NotificationRequest {
    private NotificationCategory category;
    private String title;
    private String content;

    public NotificationCommand toCommand(Long userId) {
        return new NotificationCommand(
                userId,
                title,
                category,
                content,
                ZonedDateTime.now(ZoneOffset.UTC)
        );
    }
}
