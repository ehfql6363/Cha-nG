package com.ssafy.chaing.user.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDetailInfoDTO {
    private Long id;
    private String name;
    private String nickname;
    private String profileImage;
    private Long groupId;
    private Long contractId;
}
