package com.ssafy.chaing.notification.service;

import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.common.exception.ExceptionCode;
import com.ssafy.chaing.notification.domain.NotificationCategory;
import com.ssafy.chaing.notification.domain.NotificationEntity;
import com.ssafy.chaing.notification.repository.NotificationRepository;
import com.ssafy.chaing.notification.service.command.NotificationCommand;
import com.ssafy.chaing.notification.service.command.ReadNotificationCommand;
import com.ssafy.chaing.notification.service.dto.NotificationDTO;
import com.ssafy.chaing.notification.service.dto.UnreadNotificationDTO;
import com.ssafy.chaing.user.domain.UserEntity;
import com.ssafy.chaing.user.repository.UserRepository;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final FCMService fcmService;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void publishNotification(NotificationCommand command) {

        UserEntity user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.USER_NOT_FOUND));

        NotificationEntity notification = NotificationEntity.builder()
                .user(user)
                .title(command.getTitle())
                .content(command.getContent())
                .isRead(false)
                .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
                .category(command.getCategory())
                .build();

        NotificationEntity saved = notificationRepository.save(notification);

        fcmService.sendNotificationAsync(user.getFcmToken(), command.getTitle(), command.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotifications(Long userId) {

        List<NotificationEntity> list = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return list.stream().map(NotificationDTO::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UnreadNotificationDTO getUnreadCount(Long userId) {

        long count = notificationRepository.countByUserIdAndIsReadFalse(userId);

        return new UnreadNotificationDTO(count);
    }

    @Override
    @Transactional
    public void markAsRead(ReadNotificationCommand command) {
        if (command.getNotificationIds() == null || command.getNotificationIds().isEmpty()) {
            return;
        }

        int updatedCount = notificationRepository.markAsReadByIds(
                command.getUserId(),
                command.getNotificationIds()
        );

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW) // ✅ 추가!
    public void sendNotification(Long userId, String title, String content, NotificationCategory category) {
        userRepository.findById(userId).ifPresent(user -> {
            NotificationEntity notification = NotificationEntity.builder()
                    .user(user)
                    .title(title)
                    .content(content)
                    .isRead(false)
                    .createdAt(ZonedDateTime.now(ZoneOffset.UTC))
                    .category(category)
                    .build();

            NotificationEntity saved = notificationRepository.save(notification);

            // 🔔 FCM 토큰이 있는 경우에만 푸시 전송
            if (user.getFcmToken() != null) {
                fcmService.sendNotificationAsync(user.getFcmToken(), title, content);
            } else {
                StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
                log.info("✅ 알림 저장만 완료 (푸시 생략) - userId: {}, 호출 위치: {}.{}",
                        userId, caller.getClassName(), caller.getMethodName());
            }
        });
    }
}
