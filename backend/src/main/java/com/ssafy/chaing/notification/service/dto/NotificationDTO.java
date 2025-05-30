package com.ssafy.chaing.notification.service.dto;

import com.ssafy.chaing.notification.domain.NotificationCategory;
import com.ssafy.chaing.notification.domain.NotificationEntity;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private NotificationCategory category;
    private String title;
    private String content;
    private ZonedDateTime date;
    private Boolean read;

    public static NotificationDTO from(NotificationEntity entity) {
        return new NotificationDTO(
                entity.getId(),
                entity.getCategory(),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.isRead()
        );
    }
}
