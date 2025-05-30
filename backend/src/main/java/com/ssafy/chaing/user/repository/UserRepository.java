package com.ssafy.chaing.user.repository;

import com.ssafy.chaing.user.domain.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailAddress(String emailAddress);

    @Query("""
            SELECT cu.user FROM ContractUserEntity cu
            WHERE cu.contract.id = (
                SELECT cu2.contract.id
                FROM ContractUserEntity cu2
                WHERE cu2.user.id = :userId
            )
            AND cu.user.id <> :userId
            """)
    List<UserEntity> findOtherUsersInSameContract(@Param("userId") Long userId);

    @Query("""
            SELECT cu.user
            FROM ContractUserEntity cu
            WHERE cu.contract.id = (
                SELECT cu2.contract.id
                FROM ContractUserEntity cu2
                WHERE cu2.user.id = :userId
            )
            """)
    List<UserEntity> findAllUsersInSameContract(@Param("userId") Long userId);

}
