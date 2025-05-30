package com.ssafy.chaing.rule.controller.request;

import com.ssafy.chaing.rule.dto.LifeRuleCreateForm;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeRuleFormRequest {
    List<LifeRuleCreateForm> rules;
}
