package com.ssafy.chaing.payment.service.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveRentCommand {
    private Long userId;
    private String year;
    private String month;
}
