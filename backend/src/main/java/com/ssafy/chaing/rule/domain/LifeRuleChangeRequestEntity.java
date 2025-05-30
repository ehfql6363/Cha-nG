package com.ssafy.chaing.rule.domain;

import com.ssafy.chaing.common.domain.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;


@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction(value = "is_deleted = false")
@Table(name = "life_rule_change_requests")
@Entity
public class LifeRuleChangeRequestEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "life_rule_id", nullable = false)
    private LifeRuleEntity lifeRule;  // 변경 대상 생활 룰

    @OneToMany(mappedBy = "changeRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LifeRuleChangeItemEntity> changeItems;  // 변경된 항목들

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "requested_by", nullable = false)
//    private GroupUserEntity requestedBy;  // 변경 요청한 사용자

    @Column(name = "requested_at", nullable = false)
    private ZonedDateTime requestedAt;  // 변경 요청 시간

    @Column(name = "approval_count", nullable = false)
    private int approvalCount = 1;  // 처음 생성 시 요청자가 포함되어 있으므로 기본값 1

    @Column(name = "total_group_member", nullable = false)
    private int totalGroupMember;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChangeRequestStatus status = ChangeRequestStatus.IDLE;  // 초기값은 IDLE

    public void approve(int totalGroupMembers) {
        this.approvalCount++;
        if (this.approvalCount >= totalGroupMembers) {
            this.status = ChangeRequestStatus.APPROVED;
        }
    }

    public void reject() {
        this.status = ChangeRequestStatus.PROGRESS;
    }

    public void clear() {
        this.approvalCount = 1;
        this.status = ChangeRequestStatus.IDLE;
    }

    public void inProgress() {
        this.status = ChangeRequestStatus.PROGRESS;
    }
}
