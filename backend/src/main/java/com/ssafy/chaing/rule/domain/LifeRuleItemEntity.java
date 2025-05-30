package com.ssafy.chaing.rule.domain;

import com.ssafy.chaing.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction(value = "is_deleted = false")
@Table(name = "life_rule_items")
@Entity
public class LifeRuleItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "life_rule_id", nullable = false)
    private LifeRuleEntity lifeRule;  // 하나의 생활룰에 여러 개의 요소 포함

    @Column(name = "content", nullable = false, length = 500)
    private String content;  // 생활룰 요소 내용

    @Column(name = "category", nullable = false)
    private String category;

    public void assignToLifeRule(LifeRuleEntity lifeRule) {
        this.lifeRule = lifeRule;
    }

    public void update(String newValue, String category) {
        this.content = newValue;
        this.category = category;
    }
}
