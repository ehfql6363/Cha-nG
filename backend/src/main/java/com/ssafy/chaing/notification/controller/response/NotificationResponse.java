package com.ssafy.chaing.notification.controller.response;

import com.ssafy.chaing.notification.domain.NotificationCategory;
import com.ssafy.chaing.notification.service.dto.NotificationDTO;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private NotificationCategory category;
    private String title;
    private String content;
    private Boolean read;
    private ZonedDateTime date;

    public static NotificationResponse from(NotificationDTO dto) {
        return new NotificationResponse(
                dto.getId(),
                dto.getCategory(),
                dto.getTitle(),
                dto.getContent(),
                dto.getRead(),
                dto.getDate()
        );
    }

}
