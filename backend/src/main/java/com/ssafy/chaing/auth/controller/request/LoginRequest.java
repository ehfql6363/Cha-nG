package com.ssafy.chaing.auth.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String emailAddress;
    private String password;
}
