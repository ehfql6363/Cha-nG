package com.ssafy.chaing.group.domain;

import com.ssafy.chaing.common.domain.BaseEntity;
import com.ssafy.chaing.user.domain.UserEntity;
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
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;


@Setter
@Getter
@AllArgsConstructor
@Builder
@SQLRestriction(value = "is_deleted = false")
@Entity
@Table(name = "`groups`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = true)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(name = "group_code", nullable = false)
    private String groupCode;

    @Column(name = "isActive", nullable = false)
    private boolean isActive;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "contract_id", nullable = true)
    private Long contractId;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<GroupUserEntity> members = new ArrayList<>();
}
