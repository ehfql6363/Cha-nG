package com.ssafy.chaing.duty.repository;

import com.ssafy.chaing.duty.domain.DutyEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DutyRepository extends JpaRepository<DutyEntity, Long> {
    List<DutyEntity> findByGroup_Id(Long groupId);

    @Query("""
                SELECT d 
                FROM DutyEntity d 
                LEFT JOIN FETCH d.assignees a 
                LEFT JOIN FETCH a.groupUser gu 
                LEFT JOIN FETCH gu.user 
                WHERE d.group.id = :groupId
            """)
    List<DutyEntity> findWithAssigneesByGroupId(@Param("groupId") Long groupId);

    @Query("""
                SELECT DISTINCT d FROM DutyEntity d
                LEFT JOIN FETCH d.assignees a
                LEFT JOIN FETCH a.groupUser gu
                LEFT JOIN FETCH gu.user u
                WHERE d.dutyTimeRaw = :dutyTimeRaw
            """)
    List<DutyEntity> findWithAssigneesAndUsersByDutyTimeRaw(@Param("dutyTimeRaw") String dutyTimeRaw);

    @Query("""
                SELECT DISTINCT d FROM DutyEntity d
                LEFT JOIN FETCH d.assignees a
                LEFT JOIN FETCH a.groupUser gu
                LEFT JOIN FETCH gu.user u
                WHERE d.isDeleted = false
            """)
    List<DutyEntity> findAllWithAssigneesAndUsers();
}
