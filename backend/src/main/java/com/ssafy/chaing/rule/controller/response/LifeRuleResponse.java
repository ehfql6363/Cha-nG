package com.ssafy.chaing.rule.controller.response;

import com.ssafy.chaing.rule.dto.LifeRuleDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeRuleResponse {
    List<LifeRuleDto> lifeRules;

    public static LifeRuleResponse fromDTO(List<LifeRuleDto> dtos) {
        LifeRuleResponse response = new LifeRuleResponse();
        response.setLifeRules(dtos);
        return response;
    }
}
