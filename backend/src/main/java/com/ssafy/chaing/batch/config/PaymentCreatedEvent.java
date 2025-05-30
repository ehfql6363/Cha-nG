package com.ssafy.chaing.batch.config;

import java.time.ZonedDateTime;

public record PaymentCreatedEvent(
        Long paymentId,
        ZonedDateTime collectExecution,
        ZonedDateTime ownerExecution) {
}
