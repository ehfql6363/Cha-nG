package com.ssafy.chaing.rule.domain;

import com.ssafy.chaing.common.domain.BaseEntity;
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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction(value = "is_deleted = false")
@Table(name = "life_rule_change_items")
@Entity
public class LifeRuleChangeItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "change_request_id", nullable = false)
    private LifeRuleChangeRequestEntity changeRequest;  // 어떤 변경 요청에 속하는지

    @Column(name = "rule_item_id")
    private Long ruleItemId;  // 수정 또는 삭제할 기존 룰 항목 (insert는 null 가능)

    @Column(name = "new_value")
    private String newValue;  // 변경된 내용 (update/insert 시 사용)

    @Column(name = "category")
    private String category;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ActionType actionType;  // update, delete, create 구분


    public void clear() {
        isDeleted = true;
    }
}
