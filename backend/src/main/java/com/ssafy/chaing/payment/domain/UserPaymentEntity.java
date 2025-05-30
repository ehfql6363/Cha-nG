package com.ssafy.chaing.payment.domain;

import com.ssafy.chaing.common.domain.BaseEntity;
import com.ssafy.chaing.contract.domain.ContractUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction(value = "is_deleted = false")
@Entity
@Getter
@Table(name = "user_payments")
public class UserPaymentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentEntity payment;

    @ManyToOne
    @JoinColumn(name = "contract_member_id", nullable = false)
    private ContractUserEntity contractMember;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "amount", nullable = true)
    private Integer amount;

    @Column(name = "payment_date", nullable = true)
    private ZonedDateTime paymentDate; // 납부 날짜

    // 상태 업데이트 메서드
    public void updateStatus(PaymentStatus status) {
        this.status = status;
    }

}
