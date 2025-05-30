package com.ssafy.chaing.duty.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DutyFormRequest {
    private String title;
    @Schema(hidden = true)
    private String category;
    @Schema(type = "string", example = "15:00Z", format = "time")
    private OffsetTime dutyTime;
    private String dayOfWeek;
    private boolean useTime;
    private List<Long> assignees;
}