package com.ssafy.chaing.contract.service;

import com.ssafy.chaing.contract.service.dto.CreateLivingBudgetDto;
import com.ssafy.chaing.contract.service.dto.LivingBudgetAccountDTO;

public interface BudgetService {

    void notifyLeaderToRegisterLivingAccount(Long userId);

    LivingBudgetAccountDTO getLivingAccount(Long userId);

    void saveAccountAndNotify(CreateLivingBudgetDto accountInfo);

    void notifyLivingDeposit(Long userId);

    void notifyLivingWithdraw(Long userId);

}
