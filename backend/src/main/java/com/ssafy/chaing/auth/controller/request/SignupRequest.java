package com.ssafy.chaing.auth.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String emailAddress;
    private String password;
    private String name;
    private String nickname;
}
