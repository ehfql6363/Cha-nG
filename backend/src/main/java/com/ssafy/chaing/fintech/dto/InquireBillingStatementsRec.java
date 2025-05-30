package com.ssafy.chaing.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * 청구 내역 조회 응답 DTO
 * @param billingMonth 청구 월 (YYYYMM)
 * @param billingList 주차별 청구 내역 리스트
 */
public record InquireBillingStatementsRec(
        @JsonProperty("billingMonth")
        String billingMonth,

        @JsonProperty("billingList")
        List<BillingStatementItem> billingList // billingList 필드 추가
) {
    /**
     * 주차별 청구 내역 항목 DTO
     * @param billingWeek 청구 주차
     * @param billingDate 청구 일자 (YYYYMMDD)
     * @param totalBalance 총 잔액
     * @param status 결제 상태 ("미결제", "결제완료" 등)
     * @param paymentDate 결제 일자 (YYYYMMDD), 미결제시 ""
     * @param paymentTime 결제 시간 (HHMMSS), 미결제시 ""
     */
    public record BillingStatementItem(
            @JsonProperty("billingWeek")
            String billingWeek,

            @JsonProperty("billingDate")
            String billingDate,

            @JsonProperty("totalBalance")
            String totalBalance, // JSON에서 문자열로 오므로 String 타입 사용

            @JsonProperty("status")
            String status,

            @JsonProperty("paymentDate")
            String paymentDate,

            @JsonProperty("paymentTime")
            String paymentTime
    ) {}
}