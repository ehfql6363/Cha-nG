package com.ssafy.chaing.notification.controller;

import com.ssafy.chaing.auth.domain.UserPrincipal;
import com.ssafy.chaing.common.schema.BaseResponse;
import com.ssafy.chaing.notification.controller.request.NotificationRequest;
import com.ssafy.chaing.notification.controller.request.ReadNotificationRequest;
import com.ssafy.chaing.notification.controller.response.NotificationResponse;
import com.ssafy.chaing.notification.controller.response.UnreadNotificationResponse;
import com.ssafy.chaing.notification.service.NotificationService;
import com.ssafy.chaing.notification.service.dto.UnreadNotificationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(
        name = "Notification API",
        description = "알림 관련 기능 API"
)
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 목록 조회
    @Operation(
            summary = "알림 목록 조회",
            description = "사용자의 전체 알림 목록을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<BaseResponse<List<NotificationResponse>>> getNotifications(
            @RequestParam Long userId) {

        List<NotificationResponse> notifications = notificationService.getNotifications(userId)
                .stream()
                .map(NotificationResponse::from)
                .toList();

        return ResponseEntity.ok(
                BaseResponse.success(notifications)
        );
    }

    // 읽지 않은 알림 개수 조회
    @Operation(
            summary = "읽지 않은 알림 수 조회",
            description = "읽지 않은 알림 개수를 반환합니다."
    )
    @GetMapping("/count")
    public ResponseEntity<BaseResponse<UnreadNotificationResponse>> getUnreadNotificationCount(
            @RequestParam Long userId) {

        UnreadNotificationDTO dto = notificationService.getUnreadCount(userId);
        UnreadNotificationResponse response = UnreadNotificationResponse.from(dto);

        return ResponseEntity.ok(
                BaseResponse.success(response)
        );
    }

    // 알림 읽음 처리
//    @PutMapping("/read/{notificationId}")
//    public ResponseEntity<BaseResponse<Void>> markNotificationAsRead(
//            @PathVariable Long notificationId) {
//
//        notificationService.markAsRead(notificationId);
//        return ResponseEntity.ok(BaseResponse.success(null));
//    }

    // 알림 읽음 처리
    @Operation(
            summary = "알림 읽음 처리",
            description = "알림을 읽음 상태로 처리합니다."
    )
    @PutMapping("/read")
    public ResponseEntity<BaseResponse<Void>> markNotificationAsRead(
            @RequestBody ReadNotificationRequest body,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        notificationService.markAsRead(body.toCommand(principal.getId()));

        return ResponseEntity.ok(BaseResponse.success(null));
    }

    // 알림 발행
    @Operation(
            summary = "알림 발행",
            description = "사용자에게 수동으로 알림을 발행합니다."
    )
    @PostMapping("/publish")
    public ResponseEntity<BaseResponse<Void>> publishNotification(
            @RequestBody NotificationRequest request,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        notificationService.publishNotification(request.toCommand(principal.getId()));
        return ResponseEntity.ok(BaseResponse.success(null));
    }
}
