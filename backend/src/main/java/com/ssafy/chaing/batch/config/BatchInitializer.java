package com.ssafy.chaing.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchInitializer implements ApplicationRunner {

    private final RentBatchConfig rentBatchConfig;

    @Override
    public void run(ApplicationArguments args) {
        // 어플리케이션이 시작될때 처리되지 않은 payment를 taskScheduler에 추가
        rentBatchConfig.registerExistingPayments();
    }
}

