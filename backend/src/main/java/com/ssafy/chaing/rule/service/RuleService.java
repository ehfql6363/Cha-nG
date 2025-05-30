package com.ssafy.chaing.rule.service;


import com.ssafy.chaing.rule.controller.request.LifeRuleApproveRequest;
import com.ssafy.chaing.rule.controller.request.LifeRuleFormRequest;
import com.ssafy.chaing.rule.controller.request.LifeRuleUpdateRequest;
import com.ssafy.chaing.rule.controller.response.LifeRuleResponse;
import com.ssafy.chaing.rule.controller.response.NotApproveUserResponse;
import com.ssafy.chaing.rule.dto.LifeRuleUpdateDto;
import java.util.List;

public interface RuleService {

    LifeRuleResponse createLifeRule(LifeRuleFormRequest request, Long userId);

    LifeRuleResponse getLifeRules(Long userId);

    List<LifeRuleUpdateDto> updateRules(LifeRuleUpdateRequest request, Long userId);

    List<LifeRuleUpdateDto> getUpdateLifeRule(Long userId);

    void approveLifeRule(LifeRuleApproveRequest request, Long groupUserId);

    NotApproveUserResponse getApprovedUserList(Long groupId);

    boolean isLifeRuleChangeInProgress(Long userId);

}
