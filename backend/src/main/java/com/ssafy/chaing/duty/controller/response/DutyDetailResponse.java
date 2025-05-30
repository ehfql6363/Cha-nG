package com.ssafy.chaing.duty.controller.response;

import com.ssafy.chaing.duty.domain.DutyEntity;
import java.time.OffsetTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DutyDetailResponse {
    private Long id;
    private String title;
    private String category;
    private OffsetTime dutyTime;
    private String dayOfWeek;
    private boolean useTime;
    private List<Long> assignees;

    public static DutyDetailResponse from(DutyEntity dutyEntity) {
        List<Long> assigneeIds = dutyEntity.getAssignees().stream()
                .map(assignee -> assignee.getGroupUser().getUser().getId())
                .collect(Collectors.toList());

        String rawTime = dutyEntity.getDutyTimeRaw();
        OffsetTime dutyTime = null;
        if (rawTime != null && !rawTime.isBlank()) {
            dutyTime = OffsetTime.parse(rawTime);
        }
        String category = dutyEntity.getCategory();
        return new DutyDetailResponse(
                dutyEntity.getId(),
                dutyEntity.getTitle(),
                category != null ? category : "OTHER",  // 기본값 설정
                dutyTime,
                dutyEntity.getDayOfWeek(),
                dutyEntity.isUseTime(),
                assigneeIds
        );
    }
}
