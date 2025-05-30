package com.ssafy.chaing.batch.config;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ExecutionTime {
    private Integer hour;
    private Integer minute;
    private Integer dayOffset;
    private ZonedDateTime fixedTime;

    public ExecutionTime(ZonedDateTime fixedTime) {
        this.fixedTime = fixedTime;
        this.hour = null;
        this.minute = null;
        this.dayOffset = null;
    }

    public ExecutionTime(int hour, int minute, int dayOffset) {
        this.hour = hour;
        this.minute = minute;
        this.dayOffset = dayOffset;
    }

    public ZonedDateTime calculate(int baseDayOfMonth) {
        if (fixedTime != null) {
            return fixedTime;
        }

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        ZonedDateTime targetDateTime = now.withDayOfMonth(1)
                .withHour(hour).withMinute(minute).withSecond(0).withNano(0);

        // day 설정을 안전하게 (존재하지 않는 날짜 방지)
        int maxDayOfMonth = targetDateTime.getMonth().length(targetDateTime.toLocalDate().isLeapYear());
        int safeDay = Math.min(baseDayOfMonth, maxDayOfMonth);

        targetDateTime = targetDateTime.withDayOfMonth(safeDay);

        // 이미 해당 날짜/시간이 지났다면 → 다음 달로
        if (now.isAfter(targetDateTime)) {
            targetDateTime = targetDateTime.plusMonths(1);
            maxDayOfMonth = targetDateTime.getMonth().length(targetDateTime.toLocalDate().isLeapYear());
            safeDay = Math.min(baseDayOfMonth, maxDayOfMonth);
            targetDateTime = targetDateTime.withDayOfMonth(safeDay);
        }

        return targetDateTime.plusDays(dayOffset);
    }


    public ZonedDateTime calculateFromNow() {
        if (fixedTime != null) {
            return fixedTime;
        }

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        return now
                .plusDays(dayOffset != null ? dayOffset : 0)
                .plusHours(hour != null ? hour : 0)
                .plusMinutes(minute != null ? minute : 0)
                .withSecond(0);
    }
}
