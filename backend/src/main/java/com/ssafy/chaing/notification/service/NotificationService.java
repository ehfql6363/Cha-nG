package com.ssafy.chaing.notification.service;

import com.ssafy.chaing.notification.domain.NotificationCategory;
import com.ssafy.chaing.notification.service.command.NotificationCommand;
import com.ssafy.chaing.notification.service.command.ReadNotificationCommand;
import com.ssafy.chaing.notification.service.dto.NotificationDTO;
import com.ssafy.chaing.notification.service.dto.UnreadNotificationDTO;
import java.util.List;

public interface NotificationService {
    void publishNotification(NotificationCommand command);

    List<NotificationDTO> getNotifications(Long userId);

    UnreadNotificationDTO getUnreadCount(Long userId);

    void markAsRead(ReadNotificationCommand command);

    void sendNotification(Long userId, String title, String content, NotificationCategory category);
}
