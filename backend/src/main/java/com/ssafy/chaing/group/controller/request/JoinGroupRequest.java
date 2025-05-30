package com.ssafy.chaing.group.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinGroupRequest {
    private Long groupId;
    private String nickname;
    private String profileImage;
}
