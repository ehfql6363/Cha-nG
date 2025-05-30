package com.ssafy.chaing.batch.tasklet;

import com.ssafy.chaing.batch.service.UtilityBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CollectToUtilityAccountTasklet implements Tasklet {

    private final UtilityBatchService utilityBatchService;

    @Override
    public RepeatStatus execute(@NotNull StepContribution contribution,
                                @NotNull ChunkContext chunkContext
    ) {
        log.info(">>> 이번 주 공과금 청구 내역 저장 배치 Tasklet 시작");

        utilityBatchService.collectToUtilityAccount();

        log.info("<<< 이번 주 공과금 청구 내역 저장 배치 Tasklet 완료");
        return RepeatStatus.FINISHED; // 작업 완료 후 종료
    }
}
