package com.ssafy.chaing.batch.runner; // 적절한 패키지 경로로 변경하세요

import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
@Slf4j
public class OneTimeBatchTestRunner implements ApplicationListener<ApplicationReadyEvent> {

    private final JobLauncher jobLauncher;
    private final Job utilityBillingStatementJob; // Configuration에서 정의한 Job Bean
    private final Job collectToUtilityAccountJob;
    private final TaskScheduler taskScheduler; // 지연 실행을 위한 TaskScheduler 주입
    private final Environment environment; // 현재 활성 프로파일 확인용 (선택적)

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (!List.of(environment.getActiveProfiles()).contains("test")) {
            log.info("Application Ready! 잠시 후 테스트 배치 Job들을 실행합니다.");
            // utilityBillingStatementJob을 2초 후에 실행
            taskScheduler.schedule(() -> runSpecificJob(utilityBillingStatementJob, "utilityBillingStatementJob"), Instant.now().plusSeconds(2));
            // collectToUtilityAccountJob을 5초 후에 실행 (예시: 순차 실행 보장 및 로그 분리)
            taskScheduler.schedule(() -> runSpecificJob(collectToUtilityAccountJob, "collectToUtilityAccountJob"), Instant.now().plusSeconds(5));
        } else {
            log.info("Test profile is active. Skipping automatic batch job run on startup.");
        }
    }

    // Job 객체와 이름을 받아 실행하는 메소드
    private void runSpecificJob(Job jobToRun, String jobName) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("jobName", jobName) // 어떤 잡인지 구분하기 위한 파라미터 (선택적)
                    .addLong("startTime", System.currentTimeMillis()) // 고유 파라미터
                    .toJobParameters();
            log.info(">>> ApplicationReadyEvent로 [{}] Job 실행 시작. Params: {}", jobName, jobParameters);
            jobLauncher.run(jobToRun, jobParameters);
            log.info("<<< ApplicationReadyEvent로 [{}] Job 실행 완료.", jobName);
        } catch (Exception e) {
            log.error("!!! ApplicationReadyEvent로 [{}] Job 실행 중 오류 발생", jobName, e);
        }
    }
}