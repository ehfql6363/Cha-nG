package com.ssafy.chaing.group.service.dto;

import com.ssafy.chaing.group.domain.GroupEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupDTO {
    private Long id;
    private String name;
    private String inviteCode;
    private Long leaderId;
    private Integer maxParticipant;

    public static GroupDTO from(GroupEntity entity) {
        return new GroupDTO(entity.getId(),
                entity.getName(),
                entity.getId() + "#" + entity.getGroupCode(),
                entity.getOwner().getId(),
                entity.getMaxParticipants()
        );
    }
}
