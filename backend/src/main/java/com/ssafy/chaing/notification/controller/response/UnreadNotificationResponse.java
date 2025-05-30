package com.ssafy.chaing.notification.controller.response;

import com.ssafy.chaing.notification.service.dto.UnreadNotificationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnreadNotificationResponse {
    private Long count;

    static public UnreadNotificationResponse from(UnreadNotificationDTO dto) {
        return new UnreadNotificationResponse(dto.getCount());

    }
}
