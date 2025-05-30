package com.ssafy.chaing.auth.service.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignupCommand {
    private String emailAddress;
    private String password;
    private String name;
}
