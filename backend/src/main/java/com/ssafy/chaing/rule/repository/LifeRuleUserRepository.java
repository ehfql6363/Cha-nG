package com.ssafy.chaing.rule.repository;

import com.ssafy.chaing.rule.domain.LifeRuleEntity;
import com.ssafy.chaing.rule.domain.LifeRuleUserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LifeRuleUserRepository extends JpaRepository<LifeRuleUserEntity, Long> {

    Optional<LifeRuleUserEntity> findByLifeRuleAndUserId(LifeRuleEntity lifeRule, Long userId);

    List<LifeRuleUserEntity> findByLifeRule(LifeRuleEntity lifeRule);
}
