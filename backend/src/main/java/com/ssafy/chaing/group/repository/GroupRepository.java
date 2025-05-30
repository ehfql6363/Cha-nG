package com.ssafy.chaing.group.repository;

import com.ssafy.chaing.group.domain.GroupEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

    Optional<GroupEntity> findByGroupCode(String groupCode);

    @Query("""
                SELECT DISTINCT g
                FROM GroupEntity g
                LEFT JOIN FETCH g.members m
                LEFT JOIN FETCH m.user
                WHERE g.id = :id AND g.groupCode = :groupCode
            """)
    Optional<GroupEntity> findByIdAndGroupCode(Long id, String groupCode);

    @Query("""
                SELECT DISTINCT g
                FROM GroupEntity g
                LEFT JOIN FETCH g.members m
                LEFT JOIN FETCH m.user
                WHERE g.id = :groupId
            """)
    Optional<GroupEntity> findWithMembersAndUsersById(Long groupId);
}
