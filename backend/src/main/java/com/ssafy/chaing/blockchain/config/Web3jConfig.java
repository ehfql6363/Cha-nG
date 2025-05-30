package com.ssafy.chaing.blockchain.config; // 패키지 경로는 맞게 수정하세요

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;

@Configuration
public class Web3jConfig {
    private static final Logger logger = LoggerFactory.getLogger(Web3jConfig.class);

    @Value("${web3j.client-address}")
    private String primaryClientAddress;

    @Value("${web3j.fallback-client-address}")
    private String fallbackClientAddresses;

    @Value("${web3j.connection-timeout}")
    private int connectionTimeout;

    @Value("${web3j.contract-wallet-private-key}")
    private String contractPrivateKey;

    @Value("${web3j.rent-wallet-private-key}")
    private String rentPrivateKey;

    @Value("${web3j.utility-wallet-private-key}")
    private String utilityPrivateKey;

    @Value("${web3j.chain-id}")
    private long chainId; // Web3j 5.0 이상은 long 타입 권장

    // 연결 관리에 필요한 정보들을 빈으로 등록
    @Bean
    public List<String> rpcEndpoints() {
        List<String> endpoints = new ArrayList<>();
        endpoints.add(primaryClientAddress.trim());
        if (fallbackClientAddresses != null && !fallbackClientAddresses.trim().isEmpty()) {
            Arrays.stream(fallbackClientAddresses.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .forEach(endpoints::add);
        }
        logger.info("사용할 RPC 엔드포인트 목록: {}", endpoints);
        return endpoints;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        // OkHttp 클라이언트 재사용 위해 빈으로 등록
        return new OkHttpClient.Builder()
                .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
                .readTimeout(connectionTimeout, TimeUnit.SECONDS)
                .writeTimeout(connectionTimeout, TimeUnit.SECONDS)
                .build();
    }

    @Bean
    @Qualifier("contractCredentials")
    public Credentials contractCredentials() {
        return Credentials.create(contractPrivateKey);
    }

    @Bean
    @Qualifier("rentCredentials")
    public Credentials rentCredentials() {
        return Credentials.create(rentPrivateKey);
    }

    @Bean
    @Qualifier("utilityCredentials")
    public Credentials utilityCredentials() {
        return Credentials.create(utilityPrivateKey);
    }

    @Bean
    public long chainId() {
        return chainId;
    }
}