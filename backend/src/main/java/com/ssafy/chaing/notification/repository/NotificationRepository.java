package com.ssafy.chaing.notification.repository;

import com.ssafy.chaing.notification.domain.NotificationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    long countByUserIdAndIsReadFalse(Long userId);

    // 최신 순으로 정렬된 알림 목록 조회
    List<NotificationEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

//    @Modifying
//    @Query("""
//              UPDATE NotificationEntity n
//              SET n.read = true
//              WHERE n.user.id = :userId
//              AND n.id = :notificationId
//            """)
//    int markAsReadById(Long userId, Long notificationId);

    @Modifying
    @Query("""
              UPDATE NotificationEntity n
              SET n.isRead = true
              WHERE n.user.id = :userId
              AND n.id IN :notificationIds
            """)
    int markAsReadByIds(Long userId, List<Long> notificationIds);
}
