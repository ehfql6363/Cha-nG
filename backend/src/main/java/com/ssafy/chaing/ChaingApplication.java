package com.ssafy.chaing;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
@EnableAsync
public class ChaingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChaingApplication.class, args);
    }

}
