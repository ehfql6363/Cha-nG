package com.ssafy.chaing.rule.repository;

import com.ssafy.chaing.rule.domain.LifeRuleChangeItemEntity;
import com.ssafy.chaing.rule.domain.LifeRuleChangeRequestEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LifeRuleChangeItemRepository extends JpaRepository<LifeRuleChangeItemEntity, Long> {

    List<LifeRuleChangeItemEntity> findAllByChangeRequest(LifeRuleChangeRequestEntity changeRequest);

    @Query("SELECT i FROM LifeRuleChangeItemEntity i WHERE i.changeRequest = :changeRequest AND i.isDeleted = false")
    List<LifeRuleChangeItemEntity> findNotDeletedByChangeRequest(@Param("changeRequest") LifeRuleChangeRequestEntity changeRequest);
}
