package com.ssafy.chaing.group.controller.response;

import com.ssafy.chaing.group.service.dto.GroupWithMemberDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupWithMemberResponse {
    private Long id;
    private String name;
    private String inviteCode;
    private Long leaderId;
    private Integer maxParticipants;
    private List<GroupUserResponse> members;

    public static GroupWithMemberResponse from(GroupWithMemberDTO dto) {
        return new GroupWithMemberResponse(
                dto.getId(),
                dto.getName(),
                dto.getInviteCode(),
                dto.getLeaderId(),
                dto.getMaxParticipant(),
                dto.getMembers().stream()
                        .map(GroupUserResponse::from)
                        .toList()
        );
    }
}
