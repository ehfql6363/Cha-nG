package com.ssafy.chaing.home.service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HomeOverviewDTO {
    private String groupName;
    private Boolean isRentPaid;
    private Boolean isMyRentPaid;
    private Boolean isUtilityPaid;
    private Boolean isMyUtilityPaid;
    private Boolean isLifeRuleApproved;
}
