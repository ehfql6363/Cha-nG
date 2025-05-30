package com.ssafy.chaing.rule.repository;

import com.ssafy.chaing.group.domain.GroupEntity;
import com.ssafy.chaing.rule.domain.LifeRuleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LifeRuleRepository extends JpaRepository<LifeRuleEntity, Long> {

    @EntityGraph(attributePaths = {"group", "items", "lifeRuleUsers"})
    @Query("SELECT DISTINCT l " +
            "FROM LifeRuleEntity l " +
            "JOIN l.group g " +
            "JOIN com.ssafy.chaing.group.domain.GroupUserEntity gu ON gu.group = g " +
            "WHERE gu.user.id = :userId")
    Optional<LifeRuleEntity> findByUserId(@Param("userId") Long userId);


    @EntityGraph(attributePaths = {"group", "items", "lifeRuleUsers"})
    Optional<LifeRuleEntity> findByGroup(GroupEntity group);

    @Query("SELECT lr FROM LifeRuleEntity lr " +
            "JOIN FETCH lr.lifeRuleUsers lru " +
            "JOIN FETCH lru.user " +
            "WHERE lr.group.id = :groupId")
    Optional<LifeRuleEntity> findWithUsersByGroupId(@Param("groupId") Long groupId);

    // 🔽 여기 추가
    @Query("SELECT l FROM LifeRuleEntity l WHERE l.group.id = :groupId")
    Optional<LifeRuleEntity> findByGroupId(@Param("groupId") Long groupId);
}
