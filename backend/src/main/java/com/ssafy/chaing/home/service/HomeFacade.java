package com.ssafy.chaing.home.service;

import com.ssafy.chaing.home.service.command.HomeOverviewCommand;
import com.ssafy.chaing.home.service.dto.HomeOverviewDTO;
import com.ssafy.chaing.payment.service.PaymentService;
import com.ssafy.chaing.payment.service.dto.PaymentOverviewDTO;
import com.ssafy.chaing.rule.service.RuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HomeFacade {
    private final PaymentService paymentService;
    private final RuleService ruleService;

    public HomeOverviewDTO getHomeOverview(HomeOverviewCommand command) {

        // repository 호출 없이 유저 아이디를 넘겨서 service쪽에서 조회하도록 위임
        PaymentOverviewDTO paymentOverview = paymentService.getPaymentOverview(command.getUserId());
        boolean lifeRuleChangeInProgress = ruleService.isLifeRuleChangeInProgress(command.getUserId());

        return new HomeOverviewDTO(
                paymentOverview.getGroupName(),
                paymentOverview.getRentPaymentStatus(),
                paymentOverview.getUserRentPaymentStatus(),
                paymentOverview.getUtilityPaymentStatus(),
                paymentOverview.getUserUtilityPaymentStatus(),
                lifeRuleChangeInProgress
        );

    }

}
