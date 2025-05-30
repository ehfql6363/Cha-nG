package com.ssafy.chaing.duty.controller.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DutyListResponse {
    private List<DutyDetailResponse> sunday;
    private List<DutyDetailResponse> monday;
    private List<DutyDetailResponse> tuesday;
    private List<DutyDetailResponse> wednesday;
    private List<DutyDetailResponse> thursday;
    private List<DutyDetailResponse> friday;
    private List<DutyDetailResponse> saturday;
}
