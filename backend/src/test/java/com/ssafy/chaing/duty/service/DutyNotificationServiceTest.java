package com.ssafy.chaing.duty.service;

import com.ssafy.chaing.duty.domain.DutyAssigneeEntity;
import com.ssafy.chaing.duty.domain.DutyEntity;
import com.ssafy.chaing.duty.repository.DutyRepository;
import com.ssafy.chaing.group.domain.GroupUserEntity;
import com.ssafy.chaing.notification.domain.NotificationCategory;
import com.ssafy.chaing.notification.service.NotificationService;
import com.ssafy.chaing.user.domain.UserEntity;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DutyNotificationServiceTest {

    @Mock
    private DutyRepository dutyRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private DutyNotificationService dutyNotificationService;

    @DisplayName("정시 duty 알림: useTime=true, 1시간 후 duty 알림 전송")
    @Test
    void testTimedDuty_ShouldSendOneHourBefore() {
        // 기준 시간: 2024-04-02 04:00 UTC (13:00 KST)
        ZonedDateTime testUtc = ZonedDateTime.of(2024, 4, 2, 4, 0, 0, 0, ZoneOffset.UTC);
        OffsetTime dutyTime = testUtc.plusHours(1).toOffsetDateTime().toOffsetTime(); // 05:00Z
        String dutyTimeRaw = dutyTime.toString(); // "05:00Z"
        String dayOfWeek = testUtc.plusHours(1).withZoneSameInstant(ZoneId.of("Asia/Seoul")).getDayOfWeek()
                .toString(); // TUESDAY

        DutyEntity duty = createDutyWithAssignee("정시 당번", true, dutyTimeRaw, dayOfWeek);
        Mockito.when(dutyRepository.findAllWithAssigneesAndUsers()).thenReturn(List.of(duty));

        dutyNotificationService.processDutyNotificationAt(testUtc);

        Mockito.verify(notificationService).sendNotification(
                Mockito.eq(123L),
                Mockito.eq("[당번 알림] 정시 당번"),
                Mockito.contains("1시간 후"),
                Mockito.eq(NotificationCategory.DUTY)
        );
    }

    @DisplayName("종일 duty 알림: useTime=false, KST 기준 오전 8시 알림")
    @Test
    void testFullDayDuty_At8AM_ShouldSendTodayAlert() {
        // 기준 시간: 2024-04-02 23:00 UTC (08:00 KST 4/3 수요일 아침)
        ZonedDateTime testUtc = ZonedDateTime.of(2024, 4, 2, 23, 0, 0, 0, ZoneOffset.UTC);
        String todayKstDayOfWeek = testUtc.withZoneSameInstant(ZoneId.of("Asia/Seoul")).getDayOfWeek()
                .toString(); // WEDNESDAY

        DutyEntity duty = createDutyWithAssignee("종일 당번(오늘)", false, null, todayKstDayOfWeek);
        Mockito.when(dutyRepository.findAllWithAssigneesAndUsers()).thenReturn(List.of(duty));

        dutyNotificationService.processDutyNotificationAt(testUtc);

        Mockito.verify(notificationService).sendNotification(
                Mockito.eq(123L),
                Mockito.eq("[당번 알림] 종일 당번(오늘)"),
                Mockito.contains("오늘 예정된"),
                Mockito.eq(NotificationCategory.DUTY)
        );
    }

    @DisplayName("종일 duty 알림: useTime=false, KST 기준 밤 11시 알림 (내일 알림)")
    @Test
    void testFullDayDuty_At11PM_ShouldSendTomorrowAlert() {
        // 기준 시간: 2024-04-02 14:00 UTC → 23:00 KST (화요일 밤)
        ZonedDateTime testUtc = ZonedDateTime.of(2024, 4, 2, 14, 0, 0, 0, ZoneOffset.UTC);
        String tomorrowKstDayOfWeek = testUtc.withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .plusDays(1).getDayOfWeek().toString(); // WEDNESDAY

        DutyEntity duty = createDutyWithAssignee("종일 당번(내일)", false, null, tomorrowKstDayOfWeek);
        Mockito.when(dutyRepository.findAllWithAssigneesAndUsers()).thenReturn(List.of(duty));

        dutyNotificationService.processDutyNotificationAt(testUtc);

        Mockito.verify(notificationService).sendNotification(
                Mockito.eq(123L),
                Mockito.eq("[당번 알림] 종일 당번(내일)"),
                Mockito.contains("내일 예정된"),
                Mockito.eq(NotificationCategory.DUTY)
        );
    }


    private DutyEntity createDutyWithAssignee(String title, boolean useTime, String dutyTimeRaw, String dayOfWeek) {
        // ✅ 유저
        UserEntity user = UserEntity.builder()
                .id(123L)
                .fcmToken("dummyToken")
                .build();

        // ✅ 그룹 유저
        GroupUserEntity groupUser = GroupUserEntity.builder()
                .user(user)
                .build();

        // ✅ 당번 엔티티
        DutyEntity duty = DutyEntity.builder()
                .title(title)
                .useTime(useTime)
                .dutyTimeRaw(dutyTimeRaw)
                .dayOfWeek(dayOfWeek)
                .build();

        // ✅ 당번 할당 엔티티
        DutyAssigneeEntity assignee = DutyAssigneeEntity.builder()
                .groupUser(groupUser)
                .build();

        // ✅ 관계 설정
        duty.addAssignee(assignee);

        return duty;
    }

    @DisplayName("정시 duty 알림: UTC 기준 23:00Z (KST 기준 익일 08:00)에 예정된 duty가 UTC 22:00에 알림 전송되는지 확인")
    @Test
    void testDutyAt8AMKST_ShouldTriggerAt11PMUTCPreviousDay() {
        // 기준 시간: 2024-04-02 22:00 UTC → KST 2024-04-03 07:00
        ZonedDateTime testUtc = ZonedDateTime.of(2024, 4, 2, 22, 0, 0, 0, ZoneOffset.UTC);

        // 알림 대상 시간: 1시간 후 UTC 23:00 (23:00Z)
        OffsetTime dutyTime = OffsetTime.parse("23:00Z");

        // 이 알림 대상 시간의 KST 기준 요일 = "WEDNESDAY"
        String kstDayOfWeek = testUtc.plusHours(1).withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .getDayOfWeek().toString(); // WEDNESDAY

        // 당번 생성
        DutyEntity duty = createDutyWithAssignee("KST 아침 8시 duty", true, dutyTime.toString(), kstDayOfWeek);
        Mockito.when(dutyRepository.findAllWithAssigneesAndUsers()).thenReturn(List.of(duty));

        // 알림 실행
        dutyNotificationService.processDutyNotificationAt(testUtc);

        // 검증
        Mockito.verify(notificationService).sendNotification(
                Mockito.eq(123L),
                Mockito.eq("[당번 알림] KST 아침 8시 duty"),
                Mockito.contains("1시간 후"),
                Mockito.eq(NotificationCategory.DUTY)
        );
    }

}