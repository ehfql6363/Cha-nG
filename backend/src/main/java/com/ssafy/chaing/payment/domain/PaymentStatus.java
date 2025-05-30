package com.ssafy.chaing.payment.domain;

public enum PaymentStatus {
    PENDING,          // 송금 진행 중
    COLLECTED,        // 공동 계좌로 모으기 성공
    PAID,             // 집주인에게 송금 완료
    PARTIALLY_PAID,   // 일부 실패
    RETRY_PENDING,    // 재시도 상태
    FAILED,           // 완전히 실패
    DEBT,
    STARTED
}

