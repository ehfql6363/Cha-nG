package com.ssafy.chaing.fintech.service.dto;

import com.ssafy.chaing.fintech.dto.InquireBillingStatementsRec;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentBillingStatementDTO {
    private int billingMonth;
    private Integer billingWeek;
    private Integer totalBalance;
    private Boolean status;

    public CurrentBillingStatementDTO(InquireBillingStatementsRec rec) {
        this.billingMonth = Integer.parseInt(rec.billingMonth());
        this.billingWeek = Integer.parseInt(rec.billingList().getLast().billingWeek());
        this.totalBalance = Integer.parseInt(rec.billingList().getLast().totalBalance());
        this.status = Objects.equals(rec.billingList().getLast().status(), "결제완료");
    }
}
