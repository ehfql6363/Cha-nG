package com.ssafy.chaing.home.controller.response;

import com.ssafy.chaing.home.service.dto.HomeOverviewDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class HomeOverviewResponse {
    private String groupName;
    private Boolean isRentPaid;
    private Boolean isMyRentPaid;
    private Boolean isUtilityPaid;
    private Boolean isMyUtilityPaid;
    private Boolean isLifeRuleApproved;

    public static HomeOverviewResponse from(HomeOverviewDTO dto) {
        return new HomeOverviewResponse(
                dto.getGroupName(),
                dto.getIsRentPaid(),
                dto.getIsMyRentPaid(),
                dto.getIsUtilityPaid(),
                dto.getIsMyUtilityPaid(),
                dto.getIsLifeRuleApproved()
        );
    }
}
