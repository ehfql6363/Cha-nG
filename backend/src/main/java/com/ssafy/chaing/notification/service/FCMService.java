package com.ssafy.chaing.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FCMService {
    private final FirebaseMessaging firebaseMessaging;

    public void sendNotificationAsync(String token, String title, String content) {
        CompletableFuture.runAsync(() -> {
            try {
                Message message = Message.builder()
                        .setToken(token)
                        .setNotification(Notification.builder()
                                .setTitle(title)
                                .setBody(content)
                                .build())
                        .build();

                String response = firebaseMessaging.send(message);
                StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
//                log.info("✅ FCM 발송 성공 - response: {}, 호출 위치: {}.{}", response, caller.getClassName(),
//                        caller.getMethodName());

            } catch (Exception e) {
                log.error("FCM 발송 실패 - {}", e.getMessage(), e);
            }
        }).orTimeout(5, TimeUnit.SECONDS); // 5초 타임아웃 설정
    }

}
