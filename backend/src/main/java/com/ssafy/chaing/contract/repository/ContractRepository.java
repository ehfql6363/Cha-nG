package com.ssafy.chaing.contract.repository;

import com.ssafy.chaing.contract.domain.ContractEntity;
import com.ssafy.chaing.contract.domain.ContractStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {

    @Query("""
            SELECT c FROM ContractEntity c
            JOIN FETCH c.members cu
            JOIN FETCH cu.user u
            WHERE c.id = :id
            """)
    Optional<ContractEntity> findByIdWithMembers(Long id);

    @Query("""
            SELECT DISTINCT c FROM ContractEntity c
            LEFT JOIN FETCH c.utilityCard uc
            LEFT JOIN FETCH c.members m
                        WHERE c.status = :status
            """)
    List<ContractEntity> findAllWithDetails(@Param("status") ContractStatus status);

    @Query("""
            SELECT c FROM ContractEntity c
            JOIN FETCH c.members m
            JOIN FETCH m.user u
                WHERE u.id = :userId
            """)
    Optional<ContractEntity> findByContractMemberUserId(Long userId);
}
