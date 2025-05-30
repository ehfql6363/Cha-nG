package com.ssafy.chaing.user.controller;


import com.ssafy.chaing.auth.controller.response.UserInfoResponse;
import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.common.schema.BaseResponse;
import com.ssafy.chaing.user.controller.request.UpdateUserProfileRequest;
import com.ssafy.chaing.user.service.UserService;
import com.ssafy.chaing.user.service.command.UpdateUserProfileCommand;
import com.ssafy.chaing.user.service.dto.UserDetailInfoDTO;
import com.ssafy.chaing.user.service.dto.UserProfileDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "User Controller",
        description = "사용자 정보 관리"
)
@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "나의 요약 프로필 조회",
            description = "나의 요약 프로필을 조회합니다."
    )
    @GetMapping("/me/summary")
    public ResponseEntity<BaseResponse<UserProfileDTO>> getMySummary(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        // 나의 프로필 정보 조회
        UserProfileDTO dto = userService.getMyProfile(principal.getId());
        return ResponseEntity.ok().body(BaseResponse.success(dto));
    }

    @Operation(
            summary = "나의 요약 프로필 수정",
            description = "나의 닉네임과 프로필을 수정합니다."
    )
    @PutMapping("/me/update")
    public ResponseEntity<BaseResponse<UserProfileDTO>> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UpdateUserProfileRequest body
    ) {
        // 나의 프로필 정보 업데이트
        UserProfileDTO dto = userService.updateMyProfile(
                UpdateUserProfileCommand.from(principal.getId(), body)
        );
        return ResponseEntity.ok().body(BaseResponse.success(dto));
    }

    @Operation(
            summary = "나의 계약, 그룹 정보 조회"
    )
    @GetMapping("/me/info")
    public ResponseEntity<BaseResponse<UserInfoResponse>> getUserInfo(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        // 나의 프로필 정보 업데이트
        UserDetailInfoDTO dto = userService.getUserInfo(
                principal.getId()
        );
        UserInfoResponse response = UserInfoResponse.from(dto);
        return ResponseEntity.ok().body(BaseResponse.success(response));
    }

}
