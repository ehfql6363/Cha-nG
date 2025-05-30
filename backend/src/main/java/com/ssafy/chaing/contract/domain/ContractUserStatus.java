package com.ssafy.chaing.contract.domain;

public enum ContractUserStatus {
    DRAFT,          // 계약서 작성중
    PENDING,          // 사용자 계약서 승인 대기
    CONFIRMED,        // 사용자 승인 완료
    REVIEW_REQUIRED   // 사용자가 수정하여 다시 승인 대기
}
