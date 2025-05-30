package com.ssafy.chaing.duty.domain;


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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.HashSet;
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
@Entity
@Table(name = "duties")
public class DutyEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "duty_time")
    private String dutyTimeRaw;

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek; // 수행 요일

    @Column(name = "use_time", nullable = false)
    private boolean useTime; // 시간 사용 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group; // 해당 당번이 속한 그룹

    @Builder.Default
    @OneToMany(mappedBy = "duty", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DutyAssigneeEntity> assignees = new HashSet<>(); // 할당된 사용자 목록

    // 도메인 메서드: duty 기본 정보 업데이트
    public void update(String title, String category, String dutyTime, String dayOfWeek, boolean useTime) {
        this.title = title;
        this.category = category;
        this.dutyTimeRaw = dutyTime;
        this.dayOfWeek = dayOfWeek;
        this.useTime = useTime;
    }

    // 도메인 메서드: 기존 할당자 모두 제거
    public void clearAssignees() {
        this.assignees.clear();
    }

    // 도메인 메서드: 할당자 추가
    public void addAssignee(DutyAssigneeEntity assignee) {
        this.assignees.add(assignee);
    }

}
