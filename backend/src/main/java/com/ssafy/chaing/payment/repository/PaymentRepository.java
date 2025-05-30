package com.ssafy.chaing.payment.repository;

import com.ssafy.chaing.contract.domain.ContractStatus;
import com.ssafy.chaing.payment.domain.FeeType;
import com.ssafy.chaing.payment.domain.PaymentEntity;
import com.ssafy.chaing.payment.domain.PaymentStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    List<PaymentEntity> findByFeeTypeAndStatusIn(FeeType feeType, List<PaymentStatus> statuses);

    List<PaymentEntity> findByStatus(PaymentStatus paymentStatus);

    @Query("""
            SELECT p FROM PaymentEntity p
                        JOIN FETCH p.contract c
                        JOIN FETCH c.members
                        WHERE p.id = :id
            """)
    Optional<PaymentEntity> findWithContractAndMembersById(Long id);

    List<PaymentEntity> findAllByContractIdAndFeeType(Long contractId, FeeType feeType);

    List<PaymentEntity> findAllByContractIdAndFeeTypeAndMonthOrderByWeekDesc(Long contractId, FeeType feeType,
                                                                             int month);

    Optional<PaymentEntity> findByMonth(int month);

    @Query("""
            SELECT p FROM PaymentEntity p
            JOIN FETCH p.contract c
            WHERE c.status = :status
            """)
    List<PaymentEntity> findAllPaymentsForConfirmedContracts(@Param("status") ContractStatus status);

    Optional<PaymentEntity> findWithUsersByContractIdAndMonthAndFeeType(Long contractId, int month, FeeType feeType);

    Optional<PaymentEntity> findTopByContractIdAndFeeTypeOrderByMonthDescWeekDesc(Long id, FeeType feeType);

    Optional<PaymentEntity> findTopByContractIdAndFeeTypeAndMonthOrderByWeekDesc(
            Long contractId,
            FeeType feeType,
            int month
    );

    /**
     * 특정 계약의 모든 공과금 내역을 월/주 내림차순으로 조회 (JPQL 사용)
     *
     * @param contractId 조회할 계약의 ID
     * @param feeType    조회할 요금 유형 (예: FeeType.UTILITY)
     * @return 조건에 맞는 PaymentEntity 리스트 (월 내림차순, 주 내림차순 정렬)
     */
    @Query("SELECT p FROM PaymentEntity p " +
            "WHERE p.contract.id = :contractId " +
            "AND p.feeType = :feeType " +
            "ORDER BY p.month DESC, p.week DESC")
    List<PaymentEntity> findAllByContractIdAndFeeTypeOrderByMonthDescWeekDesc(
            @Param("contractId") Long contractId,
            @Param("feeType") FeeType feeType
    );
}
