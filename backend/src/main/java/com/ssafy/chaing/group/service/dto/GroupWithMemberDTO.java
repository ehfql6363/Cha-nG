package com.ssafy.chaing.group.service.dto;

import com.ssafy.chaing.group.domain.GroupEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupWithMemberDTO {
    private Long id;
    private String name;
    private String inviteCode;
    private Long leaderId;
    private Integer maxParticipant;
    private List<GroupUserDTO> members;

    public static GroupWithMemberDTO from(GroupEntity entity) {
        return new GroupWithMemberDTO(entity.getId(),
                entity.getName(),
                entity.getId() + "#" + entity.getGroupCode(),
                entity.getOwner().getId(),
                entity.getMaxParticipants(),
                entity.getMembers().stream().map(GroupUserDTO::from).toList()
        );
    }
}
