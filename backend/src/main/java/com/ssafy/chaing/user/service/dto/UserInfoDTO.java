package com.ssafy.chaing.user.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserInfoDTO {
    private Long id;
    private String name;
    private String nickname;
}
