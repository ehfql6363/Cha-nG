package com.ssafy.chaing.contract.domain;

import com.ssafy.chaing.common.domain.BaseEntity;
import com.ssafy.chaing.group.domain.GroupEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction(value = "is_deleted = false")
@Entity
@Table(name = "contracts")
public class ContractEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "group_id", nullable = true)
    private GroupEntity group;

    @Column(name = "start_date", nullable = true)
    private ZonedDateTime startDate;

    @Column(name = "end_date", nullable = true)
    private ZonedDateTime endDate;

    @Column(name = "due_date", nullable = true) // 매월 며칠에 납부할지 (ex. 10일)
    private Integer dueDate;

    @Column(name = "owner_account_no", nullable = true) // 집주인 계좌
    private String ownerAccountNo;

    @Column(name = "rent_account_no", nullable = true) // 월세 필수 계좌
    private String rentAccountNo;

    @Column(name = "live_account_no", nullable = true) // 생활비 계좌
    private String liveAccountNo;

    @JoinColumn(name = "utility_card_id", nullable = true)
    @OneToOne
    private UtilityCardEntity utilityCard;

    @Column(name = "total_rent_ratio", nullable = true) // 3
    private Integer totalRentRatio;

    @Column(name = "utility_ratio", nullable = true) // 2
    private Integer utilityRatio;

    @Column(name = "rent_total_amount", nullable = true) // 월세 총액
    private Integer rentTotalAmount;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.PERSIST)
    private List<ContractUserEntity> members = new ArrayList<>();

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    @Column(name = "completed_at")
    private ZonedDateTime completedAt;

    @Column(name = "is_created_pdf", nullable = false)
    private Boolean isCreatedPdf;

    public void updateCompletedStatus() {
        boolean allConfirmed = members.stream()
                .filter(user -> !user.isSurplusUser())
                .allMatch(user -> user.getContractStatus() == ContractUserStatus.CONFIRMED);

        this.completed = allConfirmed;

        if (allConfirmed) {
            this.completedAt = ZonedDateTime.now(ZoneId.of("UTC"));
            this.status = ContractStatus.CONFIRMED;
        }
    }

    public void add(ContractUserEntity contractUser) {
        members.add(contractUser);
    }

    public void remove(ContractUserEntity contractUser) {
        members.remove(contractUser);
    }

    public void addAll(List<ContractUserEntity> contractUsers) {
        this.members.addAll(contractUsers);
    }

    public void removeAll(List<ContractUserEntity> contractUsers) {
        this.members.removeAll(contractUsers);
    }
}

