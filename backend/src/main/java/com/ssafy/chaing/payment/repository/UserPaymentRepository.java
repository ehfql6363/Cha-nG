package com.ssafy.chaing.payment.repository;

import com.ssafy.chaing.payment.domain.UserPaymentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPaymentRepository extends JpaRepository<UserPaymentEntity, Long> {

    @Query("SELECT up FROM UserPaymentEntity up " +
            "JOIN FETCH up.contractMember cm " +    // contractMember를 즉시 로딩
            "JOIN FETCH cm.user u " +               // contractMember의 user를 즉시 로딩
            "WHERE up.payment.id IN :paymentIds")
    List<UserPaymentEntity> findAllByPaymentIdIn(@Param("paymentIds") List<Long> paymentIds);

    @Query("""
            select up from UserPaymentEntity up
            join fetch up.contractMember cm
            join fetch cm.user where up.payment.id = :paymentId
            """)
    List<UserPaymentEntity> findWithMemberAndUserByPaymentId(Long paymentId);

    Optional<UserPaymentEntity> findByPaymentIdAndContractMemberId(Long paymentId, Long contractUserId);

    List<UserPaymentEntity> findByPaymentId(Long id);

}
