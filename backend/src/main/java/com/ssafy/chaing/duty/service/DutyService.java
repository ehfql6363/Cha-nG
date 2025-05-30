package com.ssafy.chaing.duty.service;

import com.ssafy.chaing.duty.controller.request.DutyFormRequest;
import com.ssafy.chaing.duty.controller.response.DutyDetailResponse;
import com.ssafy.chaing.duty.controller.response.DutyListResponse;
import com.ssafy.chaing.duty.controller.response.RemovedDutyResponse;
public interface DutyService {
    DutyListResponse getDuties(Long groupId);

    DutyDetailResponse creatDuty(Long groupId, DutyFormRequest request);

    DutyDetailResponse updateDuty(Long dutyId, DutyFormRequest request);

    RemovedDutyResponse removeDuty(Long dutyId);

}
