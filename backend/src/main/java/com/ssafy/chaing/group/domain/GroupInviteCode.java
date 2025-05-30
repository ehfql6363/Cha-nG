package com.ssafy.chaing.group.domain;

import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class GroupInviteCode {

    private final Long groupId;
    private final String groupCode;

    public GroupInviteCode(String inviteCode) {
        if (inviteCode == null || !inviteCode.contains("#")) {
            throw new BadRequestException(ExceptionCode.GROUP_INVITE_CODE_INVALID);
        }

        String[] parts = inviteCode.split("#");
        if (parts.length != 2) {
            throw new BadRequestException(ExceptionCode.GROUP_INVITE_CODE_INVALID);
        }

        try {
            this.groupId = Long.valueOf(parts[0]);
        } catch (NumberFormatException e) {
            throw new BadRequestException(ExceptionCode.GROUP_INVITE_CODE_INVALID);
        }

        this.groupCode = parts[1];
    }
}

