package com.ssafy.chaing.payment.service.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveUtilityCommand {
    private Long userId;
    private String year;
    private String month;
}
