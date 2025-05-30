package com.ssafy.chaing.duty.service;

import com.ssafy.chaing.common.exception.BadRequestException;
import com.ssafy.chaing.common.exception.ExceptionCode;
import com.ssafy.chaing.common.util.GPTUtil;
import com.ssafy.chaing.duty.controller.request.DutyFormRequest;
import com.ssafy.chaing.duty.controller.response.DutyDetailResponse;
import com.ssafy.chaing.duty.controller.response.DutyListResponse;
import com.ssafy.chaing.duty.controller.response.RemovedDutyResponse;
import com.ssafy.chaing.duty.domain.DutyAssigneeEntity;
import com.ssafy.chaing.duty.domain.DutyEntity;
import com.ssafy.chaing.duty.repository.DutyRepository;
import com.ssafy.chaing.group.domain.GroupEntity;
import com.ssafy.chaing.group.domain.GroupUserEntity;
import com.ssafy.chaing.group.repository.GroupRepository;
import com.ssafy.chaing.group.repository.GroupUserRepository;
import com.ssafy.chaing.notification.domain.NotificationCategory;
import com.ssafy.chaing.notification.service.NotificationService;
import com.ssafy.chaing.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DutyServiceImpl implements DutyService {

    private final DutyRepository dutyRepository;
    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final GPTUtil gptUtil;

    @Override
    public DutyListResponse getDuties(Long groupId) {
        List<DutyEntity> duties = dutyRepository.findByGroup_Id(groupId);

        Map<String, List<DutyDetailResponse>> groupedDuties = duties.stream()
                .collect(Collectors.groupingBy(
                        duty -> duty.getDayOfWeek().toLowerCase(),
                        Collectors.mapping(DutyDetailResponse::from, Collectors.toList())
                ));

        return new DutyListResponse(
                groupedDuties.getOrDefault("sunday", new ArrayList<>()),
                groupedDuties.getOrDefault("monday", new ArrayList<>()),
                groupedDuties.getOrDefault("tuesday", new ArrayList<>()),
                groupedDuties.getOrDefault("wednesday", new ArrayList<>()),
                groupedDuties.getOrDefault("thursday", new ArrayList<>()),
                groupedDuties.getOrDefault("friday", new ArrayList<>()),
                groupedDuties.getOrDefault("saturday", new ArrayList<>())
        );
    }

    @Override
    @Transactional
    public DutyDetailResponse creatDuty(Long groupId, DutyFormRequest request) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.GROUP_NOT_FOUND));

        OffsetTime dutyTime = request.getDutyTime();

        String category = gptUtil.classifyDutyContent(request.getTitle());
        if (category == null || category.isBlank()) {
            category = "OTHER";
        }

        DutyEntity dutyEntity = DutyEntity.builder()
                .title(request.getTitle())
                .category(category)
                .dutyTimeRaw(dutyTime != null ? dutyTime.toString() : null)
                .dayOfWeek(request.getDayOfWeek())
                .useTime(request.isUseTime())
                .group(group)
                .build();

        if (request.getAssignees() != null) {
            for (Long userId : request.getAssignees()) {
                if (!groupUserRepository.existsByGroupIdAndUserId(groupId, userId)) {
                    throw new BadRequestException(ExceptionCode.USER_NOT_IN_GROUP);
                }
                GroupUserEntity groupUser = groupUserRepository.findByGroupId(groupId).stream()
                        .filter(gu -> gu.getUser().getId().equals(userId))
                        .findFirst()
                        .orElseThrow(() -> new BadRequestException(ExceptionCode.USER_NOT_FOUND));
                DutyAssigneeEntity assignee = DutyAssigneeEntity.create(dutyEntity, groupUser);
                dutyEntity.addAssignee(assignee);
            }
        }

        DutyEntity savedDuty = dutyRepository.save(dutyEntity);

        // 알림: 당번 생성
        if (request.getAssignees() != null) {
            for (Long userId : request.getAssignees()) {
                notificationService.sendNotification(userId,
                        "새로운 당번 할당",
                        "당번 [" + dutyEntity.getTitle() + "] 가 할당되었습니다.",
                        NotificationCategory.DUTY);
            }
        }

        return DutyDetailResponse.from(savedDuty);
    }

    @Override
    @Transactional
    public DutyDetailResponse updateDuty(Long dutyId, DutyFormRequest request) {
        DutyEntity dutyEntity = dutyRepository.findById(dutyId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.DUTY_NOT_FOUND));

        String dutyTimeRaw = request.getDutyTime() != null ? request.getDutyTime().toString() : null;

        dutyEntity.update(
                request.getTitle(),
                gptUtil.classifyDutyContent(request.getTitle()),
                dutyTimeRaw,
                request.getDayOfWeek(),
                request.isUseTime()
        );

        dutyEntity.clearAssignees();

        if (request.getAssignees() != null) {
            for (Long userId : request.getAssignees()) {
                if (!groupUserRepository.existsByGroupIdAndUserId(dutyEntity.getGroup().getId(), userId)) {
                    throw new BadRequestException(ExceptionCode.USER_NOT_IN_GROUP);
                }
                GroupUserEntity groupUser = groupUserRepository.findByGroupId(dutyEntity.getGroup().getId()).stream()
                        .filter(gu -> gu.getUser().getId().equals(userId))
                        .findFirst()
                        .orElseThrow(() -> new BadRequestException(ExceptionCode.USER_NOT_FOUND));
                DutyAssigneeEntity assignee = DutyAssigneeEntity.create(dutyEntity, groupUser);
                dutyEntity.addAssignee(assignee);
            }
        }

        DutyEntity updatedDuty = dutyRepository.save(dutyEntity);

        //알림: 당번 수정
        if (request.getAssignees() != null) {
            for (Long userId : request.getAssignees()) {
                notificationService.sendNotification(userId,
                        "당번 수정",
                        "당번 [" + updatedDuty.getTitle() + "] 의 내용이 수정되었습니다.",
                        NotificationCategory.DUTY
                );
            }
        }

        return DutyDetailResponse.from(updatedDuty);
    }

    @Override
    @Transactional
    public RemovedDutyResponse removeDuty(Long dutyId) {
        DutyEntity dutyEntity = dutyRepository.findById(dutyId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.DUTY_NOT_FOUND));

        // 알림: 당번 삭제
        for (DutyAssigneeEntity assignee : dutyEntity.getAssignees()) {
            notificationService.sendNotification(
                    assignee.getGroupUser().getUser().getId(),
                    "당번 삭제",
                    "당번 [" + dutyEntity.getTitle() + "] 이 삭제되었습니다.",
                    NotificationCategory.DUTY
            );
        }

        dutyRepository.delete(dutyEntity);
        return new RemovedDutyResponse(dutyId);
    }

}
