package com.ssafy.chaing.rule.controller.request;

import com.ssafy.chaing.rule.dto.LifeRuleUpdateForm;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeRuleUpdateRequest {
    List<LifeRuleUpdateForm> updates;
}
