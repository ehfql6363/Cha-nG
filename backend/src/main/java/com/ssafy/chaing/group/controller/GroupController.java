package com.ssafy.chaing.group.controller;

import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.common.schema.BaseResponse;
import com.ssafy.chaing.group.controller.request.CreateGroupRequest;
import com.ssafy.chaing.group.controller.request.JoinGroupRequest;
import com.ssafy.chaing.group.controller.response.GroupResponse;
import com.ssafy.chaing.group.controller.response.GroupWithMemberResponse;
import com.ssafy.chaing.group.service.GroupService;
import com.ssafy.chaing.group.service.command.CreateGroupCommand;
import com.ssafy.chaing.group.service.command.JoinGroupCommand;
import com.ssafy.chaing.group.service.dto.GroupDTO;
import com.ssafy.chaing.group.service.dto.GroupWithMemberDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Group Controller",
        description = "그룹 생성 및 정보 관리"
)
@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    private final GroupService groupService;

    @Operation(
            summary = "그룹 생성",
            description = "새로운 그룹을 생성하고, 생성자를 그룹장으로 등록합니다."
    )
    @PostMapping
    public ResponseEntity<BaseResponse<GroupResponse>> createGroup(@RequestBody CreateGroupRequest body,
                                                                   @AuthenticationPrincipal UserPrincipal principal
    ) {
        GroupResponse response = GroupResponse.from(groupService.createGroup(
                new CreateGroupCommand(Long.valueOf(principal.getUsername()),
                        body.getOwnerNickname(),
                        body.getOwnerProfileImage(),
                        body.getGroupName(),
                        body.getMaxParticipants()
                )
        ));

        return ResponseEntity.created(URI.create("/groups/" + response.getId()))
                .body(BaseResponse.success(response));
    }

    @Operation(
            summary = "그룹 정보 조회",
            description = "그룹 ID로 그룹 정보를 조회합니다."
    )
    @GetMapping("/{groupId}")
    public ResponseEntity<BaseResponse<GroupWithMemberResponse>> getGroup(@PathVariable Long groupId) {
        GroupWithMemberDTO dto = groupService.getGroup(groupId);

        GroupWithMemberResponse response = GroupWithMemberResponse.from(dto);

        return ResponseEntity.ok()
                .body(BaseResponse.success(response));
    }

    @Operation(
            summary = "초대 코드로 그룹 조회",
            description = "초대 코드를 통해 그룹 정보를 조회합니다."
    )
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<GroupWithMemberResponse>> getGroupByInviteCode(
            @Parameter(description = "초대 코드", required = true) @RequestParam(name = "inviteCode") String inviteCode
    ) {

        GroupWithMemberDTO dto = groupService.getGroupByInviteCode(inviteCode);

        GroupWithMemberResponse response = GroupWithMemberResponse.from(dto);

        return ResponseEntity.ok()
                .body(BaseResponse.success(response));

    }

    @Operation(
            summary = "그룹 참여",
            description = "초대 코드를 통해 그룹에 참여합니다."
    )
    @PostMapping("/join")
    public ResponseEntity<BaseResponse<GroupResponse>> joinGroups(@RequestBody JoinGroupRequest body,
                                                                  @AuthenticationPrincipal UserPrincipal principal) {
        GroupDTO groupDTO = groupService.joinGroup(new JoinGroupCommand(Long.valueOf(principal.getUsername()),
                        body.getGroupId(),
                        body.getNickname(),
                        body.getProfileImage()
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(GroupResponse.from(groupDTO)));


    }

}
