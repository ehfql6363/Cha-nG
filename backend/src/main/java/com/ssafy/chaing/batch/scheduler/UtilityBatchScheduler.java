package com.ssafy.chaing.batch.scheduler;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UtilityBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job utilityBillingStatementJob;
    private final Job collectToUtilityAccountJob;

    @Scheduled(cron = "0 10 10 * * MON", zone = "Asia/Seoul")
    public void runUtilityBillingJob() {
        try {
            // Job 실행 시 파라미터 전달 (동일 파라미터로 재실행 방지 및 실행 기록 구분용)
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis())) // 현재 시간을 파라미터로 추가
                    .addString("uuid", UUID.randomUUID().toString()) // UUID 추가로 고유성 보장
                    .toJobParameters();

            log.info(">>> 스케줄러 실행: Utility Billing Job 시작. Params: {}", jobParameters);
            jobLauncher.run(utilityBillingStatementJob, jobParameters); // Job 실행
            log.info("<<< 스케줄러 실행: Utility Billing Job 완료.");

        } catch (Exception e) {
            log.error("!!! 스케줄러 실행 중 오류 발생: Utility Billing Job 실패", e);
        }
    }

    @Scheduled(cron = "0 0 17 * * THU", zone = "Asia/Seoul") // KST 기준 목요일 17시 0분 0초
    public void runCollectToUtilityAccountJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis())) // 현재 시간을 파라미터로 추가
                    .addString("uuid", UUID.randomUUID().toString()) // UUID 추가로 고유성 보장
                    .toJobParameters();

            log.info(">>> 스케줄러 실행: Collect To Utility Account Job 시작. Params: {}", jobParameters);
            jobLauncher.run(collectToUtilityAccountJob, jobParameters); // Job 실행
            log.info("<<< 스케줄러 실행: Collect To Utility Account Job 완료.");
        } catch (Exception e) {
            log.error("!!! 스케줄러 실행 중 오류 발생: Collect To Utility Account Job 실패", e);
        }
    }
}
