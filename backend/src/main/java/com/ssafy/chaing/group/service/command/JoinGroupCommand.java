package com.ssafy.chaing.group.service.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinGroupCommand {
    private Long userId;
    private Long groupId;
    private String nickname;
    private String profileImage;
}
