package com.ssafy.chaing.group.service;

import com.ssafy.chaing.group.service.command.CreateGroupCommand;
import com.ssafy.chaing.group.service.command.JoinGroupCommand;
import com.ssafy.chaing.group.service.dto.GroupDTO;
import com.ssafy.chaing.group.service.dto.GroupWithMemberDTO;

public interface GroupService {
    GroupDTO createGroup(CreateGroupCommand command);

    GroupWithMemberDTO getGroup(Long groupId);

    GroupDTO joinGroup(JoinGroupCommand command);

    GroupWithMemberDTO getGroupByInviteCode(String inviteCode);
}
