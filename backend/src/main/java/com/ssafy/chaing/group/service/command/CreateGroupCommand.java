package com.ssafy.chaing.group.service.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateGroupCommand {
    private Long userId;
    private String ownerNickname;
    private String ownerProfileImage;
    private String groupName;
    private Integer maxParticipants;
}
