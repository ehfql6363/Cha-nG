package com.ssafy.chaing.rule.repository;

import com.ssafy.chaing.rule.domain.ChangeRequestStatus;
import com.ssafy.chaing.rule.domain.LifeRuleChangeRequestEntity;
import com.ssafy.chaing.rule.domain.LifeRuleEntity;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LifeRuleChangeRequestRepository extends JpaRepository<LifeRuleChangeRequestEntity, Long> {

    Optional<LifeRuleChangeRequestEntity> findByLifeRuleAndStatus(LifeRuleEntity lifeRule, ChangeRequestStatus status);

    //    @EntityGraph(attributePaths = {"changeItems"})
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT r
            FROM LifeRuleChangeRequestEntity r
            WHERE r.lifeRule = :lifeRule
              AND r.status = :status
            """)
    Optional<LifeRuleChangeRequestEntity> findWithLockByLifeRuleAndStatus(
            @Param("lifeRule") LifeRuleEntity lifeRule,
            @Param("status") ChangeRequestStatus status);

    Optional<LifeRuleChangeRequestEntity> findByLifeRule(LifeRuleEntity lifeRule);

    @Query("""
            SELECT r FROM LifeRuleChangeRequestEntity r
            JOIN FETCH r.lifeRule lr
            JOIN FETCH lr.lifeRuleUsers lru
            JOIN FETCH lru.user u
            WHERE lr.group.id = :groupId AND r.status = :status
            """)
    Optional<LifeRuleChangeRequestEntity> findProgressingRequestWithUsersByGroupId(
            @Param("groupId") Long groupId,
            @Param("status") ChangeRequestStatus status
    );

    @Query("""
            select req
            from LifeRuleChangeRequestEntity req
            join fetch req.lifeRule lr
            join fetch lr.group g
            where g.id = :groupId and req.status = 'PROGRESS'
            """)
    Optional<LifeRuleChangeRequestEntity> findProgressingRequestByGroupId(@Param("groupId") Long groupId);
}
