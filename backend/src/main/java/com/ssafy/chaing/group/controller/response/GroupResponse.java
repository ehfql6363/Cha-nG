package com.ssafy.chaing.group.controller.response;

import com.ssafy.chaing.group.service.dto.GroupDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponse {
    private Long id;
    private String name;
    private String inviteCode;
    private Long leaderId;
    private Integer maxParticipants;

    public static GroupResponse from(GroupDTO dto) {
        return new GroupResponse(
                dto.getId(),
                dto.getName(),
                dto.getInviteCode(),
                dto.getLeaderId(),
                dto.getMaxParticipant()
        );
    }
}
