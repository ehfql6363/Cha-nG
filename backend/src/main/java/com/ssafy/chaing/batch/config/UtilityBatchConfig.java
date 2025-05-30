package com.ssafy.chaing.batch.config;

import com.ssafy.chaing.batch.tasklet.CollectToUtilityAccountTasklet;
import com.ssafy.chaing.batch.tasklet.SaveBillingStatementTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class UtilityBatchConfig {
    private final SaveBillingStatementTasklet saveBillingStatementTasklet;
    private final CollectToUtilityAccountTasklet collectToUtilityAccountTasklet;

    // Job 정의
    @Bean
    public Job utilityBillingStatementJob(JobRepository jobRepository, Step saveCurrentWeekBillingStatementStep) {
        log.info("--- Utility Billing Statement Job 빈 등록 ---");
        return new JobBuilder("utilityBillingStatementJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // Job 실행 시마다 ID 자동 증가 (동일 파라미터 재실행 가능)
                .start(saveCurrentWeekBillingStatementStep) // 실행할 Step 지정
                .build();
    }

    // Step 정의
    @Bean
    public Step saveCurrentWeekBillingStatementStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("--- Save Current Week Billing Statement Step 빈 등록 ---");
        return new StepBuilder("saveCurrentWeekBillingStatementStep", jobRepository)
                .tasklet(saveBillingStatementTasklet, transactionManager) // Tasklet과 트랜잭션 매니저 설정
                // .allowStartIfComplete(true) // 필요 시, 완료된 스텝도 재시작 가능하게 설정
                .build();
    }

    @Bean
    public Job collectToUtilityAccountJob(JobRepository jobRepository, Step collectToUtilityAccountStep) {
        log.info("--- Collect To Utility Account Job 빈 등록 ---");
        return new JobBuilder("collectToUtilityAccountJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // Job 실행 시마다 ID 자동 증가 (동일 파라미터 재실행 가능)
                .start(collectToUtilityAccountStep) // 실행할 Step 지정
                .build();
    }

    // Step 정의
    @Bean
    public Step collectToUtilityAccountStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("--- Collect To Utility Account Step 빈 등록 ---");
        return new StepBuilder("collectToUtilityAccountStep", jobRepository)
                .tasklet(collectToUtilityAccountTasklet, transactionManager) // Tasklet과 트랜잭션 매니저 설정
                .build();
    }
}
