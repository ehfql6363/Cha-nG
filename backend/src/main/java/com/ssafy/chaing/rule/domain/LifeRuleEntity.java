package com.ssafy.chaing.rule.domain;

import com.ssafy.chaing.common.domain.BaseEntity;
import com.ssafy.chaing.group.domain.GroupEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Set;
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
@Table(name = "life_rules")
@Entity
public class LifeRuleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group;

    @OneToMany(mappedBy = "lifeRule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LifeRuleItemEntity> items;

    @Column(name = "change_request_id", nullable = true)
    private Long changeRequestId;

    @OneToMany(mappedBy = "lifeRule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LifeRuleUserEntity> lifeRuleUsers;

    public void setItems(Set<LifeRuleItemEntity> items) {
        this.items = items;
        for (LifeRuleItemEntity item : items) {
            item.assignToLifeRule(this);
        }
    }

    public void setLifeRuleUsers(Set<LifeRuleUserEntity> lifeRuleUsers) {
        this.lifeRuleUsers = lifeRuleUsers;
         for (LifeRuleUserEntity user : lifeRuleUsers) {
             user.settingLifeRule(this);
         }
    }

}
