package com.ssafy.chaing.rule.dto;

import com.ssafy.chaing.rule.domain.ActionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeRuleUpdateForm {
    private Long id;
    private ActionType actionType;
    private String content;
    private String category;
}
