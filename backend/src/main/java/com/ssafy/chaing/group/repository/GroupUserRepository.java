package com.ssafy.chaing.group.repository;

import com.ssafy.chaing.group.domain.GroupUserEntity;
import com.ssafy.chaing.user.domain.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUserEntity, Long> {

    boolean existsByGroupIdAndUserId(Long groupId, Long userId);

    @Query("""
             SELECT gu
             FROM GroupUserEntity gu
             JOIN FETCH gu.user u
             JOIN FETCH gu.group g
             WHERE gu.group.id = :groupId
            """)
    List<GroupUserEntity> findByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT COUNT(gu) FROM GroupUserEntity gu WHERE gu.group.id = :groupId")
    int countByGroupId(@Param("groupId") Long groupId);

    @Query("""
            SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END
            FROM GroupUserEntity u
            WHERE u.group.id = :groupId
            AND u.user.nickname = :nickname
            """)
    boolean existsByGroupIdAndUserNickname(@Param("groupId") Long groupId, @Param("nickname") String nickname);

    @Query("""
            SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END
            FROM GroupUserEntity u
            WHERE u.group.id = :groupId
            AND u.user.profileImage = :profileImage
            """)
    boolean existsByGroupIdAndUserProfileImage(@Param("groupId") Long groupId,
                                               @Param("profileImage") String profileImage);

    @Query("""
            SELECT DISTINCT gu.user
            FROM GroupUserEntity gu
            JOIN gu.user
            WHERE gu.group.id = (
                SELECT gu2.group.id
                FROM GroupUserEntity gu2
                WHERE gu2.user.id = :userId
            )
            """)
    Set<UserEntity> findAllUsersInGroupByUserId(@Param("userId") Long userId);

    @Query("""
            SELECT gu
            FROM GroupUserEntity gu
            JOIN FETCH gu.group
            WHERE gu.user.id = :userId
            """)
    Optional<GroupUserEntity> findByUserIdWithGroup(@Param("userId") Long userId);

    @Query("""
            SELECT g.owner.id
            FROM GroupUserEntity gu
            JOIN gu.group g
            WHERE gu.user.id = :userId
            """)
    Optional<Long> findGroupOwnerIdByUserId(@Param("userId") Long userId);

}
