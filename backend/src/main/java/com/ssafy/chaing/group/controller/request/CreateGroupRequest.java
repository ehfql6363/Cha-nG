package com.ssafy.chaing.group.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGroupRequest {
    private String groupName;
    private Integer maxParticipants;
    private String ownerNickname;
    private String ownerProfileImage;
}
