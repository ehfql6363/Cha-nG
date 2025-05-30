package com.ssafy.chaing.rule.domain;

import com.ssafy.chaing.user.domain.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "life_rule_user")
@Entity
public class LifeRuleUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "life_rule_id", nullable = false)
    private LifeRuleEntity lifeRule;

    @Column(name = "is_voted")
    private boolean isVoted;

    public void settingLifeRule(LifeRuleEntity lifeRule) {
        this.lifeRule = lifeRule;
    }

    public void setVoted(boolean isVoted) {
        this.isVoted = isVoted;
    }

}
