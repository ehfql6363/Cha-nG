package com.ssafy.chaing.duty.service;

import com.ssafy.chaing.duty.domain.DutyAssigneeEntity;
import com.ssafy.chaing.duty.domain.DutyEntity;
import com.ssafy.chaing.duty.repository.DutyRepository;
import com.ssafy.chaing.notification.domain.NotificationCategory;
import com.ssafy.chaing.notification.service.NotificationService;
import com.ssafy.chaing.user.domain.UserEntity;
import java.time.DayOfWeek;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DutyNotificationService {

    private final DutyRepository dutyRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 * * * *") // 매 정각마다 (UTC 기준)
    @Transactional
    public void checkAndSendDutyNotifications() {
        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
        processDutyNotificationAt(nowUtc);
    }

    public void processDutyNotificationAt(ZonedDateTime nowUtc) {
        ZonedDateTime kstNow = nowUtc.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        ZonedDateTime utcPlusOne = nowUtc.plusHours(1);
        OffsetTime targetOffsetTime = utcPlusOne.toOffsetDateTime().toOffsetTime();
        DayOfWeek targetKstDayOfWeek = utcPlusOne.withZoneSameInstant(ZoneId.of("Asia/Seoul")).getDayOfWeek();

        log.info("📌 [알림 체크] UTC 기준 시간: {}, KST 기준 요일: {}", targetOffsetTime, targetKstDayOfWeek);

        List<DutyEntity> allDuties = dutyRepository.findAllWithAssigneesAndUsers();

        // ✅ useTime == true : 정시 duty 알림
        for (DutyEntity duty : allDuties) {
            if (!duty.isUseTime()) {
                continue;
            }

            // 저장된 dutyTimeRaw 값과 비교할 때, 나노초를 0으로 맞춰서 비교합니다.
            OffsetTime dutyTime = OffsetTime.parse(duty.getDutyTimeRaw()).withNano(0);
            OffsetTime comparedTargetTime = targetOffsetTime.withNano(0);
            if (!comparedTargetTime.equals(dutyTime)) {
                continue;
            }

            if (!duty.getDayOfWeek().equalsIgnoreCase(targetKstDayOfWeek.toString())) {
                continue;
            }

            sendDutyNotificationToAllAssignees(duty,
                    "[당번 알림] " + duty.getTitle(),
                    "1시간 후 \"" + duty.getTitle() + "\" 예정되어 있습니다.");
        }

        // ✅ useTime == false : 종일 duty 알림 (KST 기준 08시 또는 23시)
        int hour = kstNow.getHour();
        if (hour == 8 || hour == 23) {
            DayOfWeek targetDay = (hour == 23)
                    ? kstNow.plusDays(1).getDayOfWeek()
                    : kstNow.getDayOfWeek();

            String timeNotice = (hour == 23) ? "내일 예정된" : "오늘 예정된";

            for (DutyEntity duty : allDuties) {
                if (duty.isUseTime()) {
                    continue;
                }
                if (!duty.getDayOfWeek().equalsIgnoreCase(targetDay.toString())) {
                    continue;
                }

                sendDutyNotificationToAllAssignees(duty,
                        "[당번 알림] " + duty.getTitle(),
                        timeNotice + " \"" + duty.getTitle() + "\" 당번이 있습니다.");
            }
        }
    }

    private void sendDutyNotificationToAllAssignees(DutyEntity duty, String title, String content) {
        for (DutyAssigneeEntity assignee : duty.getAssignees()) {
            UserEntity user = assignee.getGroupUser().getUser();
            if (user.getFcmToken() != null && !user.getFcmToken().isBlank()) {
                notificationService.sendNotification(
                        user.getId(),
                        title,
                        content,
                        NotificationCategory.DUTY
                );
                log.info("📨 알림 전송: userId={}, title={}", user.getId(), title);
            } else {
                log.warn("⚠️ FCM 토큰 없음 - userId={}", user.getId());
            }
        }
    }
}
