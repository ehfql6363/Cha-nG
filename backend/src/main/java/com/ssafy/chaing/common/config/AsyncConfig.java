package com.ssafy.chaing.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean(name = "taskExecutor") // 빈 이름 지정 가능
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);      // 기본 스레드 수
        executor.setMaxPoolSize(30);     // 최대 스레드 수
        executor.setQueueCapacity(100);   // 작업 큐 용량
        executor.setThreadNamePrefix("Async-"); // 스레드 이름 접두사
        executor.initialize();
        return executor;
    }
}

