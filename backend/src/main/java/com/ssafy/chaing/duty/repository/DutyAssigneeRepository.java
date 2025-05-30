package com.ssafy.chaing.duty.repository;

import com.ssafy.chaing.duty.domain.DutyAssigneeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DutyAssigneeRepository extends JpaRepository<DutyAssigneeEntity, Long> {
}
