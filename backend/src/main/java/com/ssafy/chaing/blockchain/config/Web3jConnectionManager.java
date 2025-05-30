package com.ssafy.chaing.blockchain.config;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.PostConstruct;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

@Component
public class Web3jConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(Web3jConnectionManager.class);

    private final List<String> rpcEndpoints;
    private final OkHttpClient okHttpClient; // Web3jConfig에서 생성된 빈 주입
    private final ReentrantLock connectionLock = new ReentrantLock(); // 동시 재연결 시도 방지용 락
    private volatile Web3j currentWeb3j; // 현재 활성 Web3j 인스턴스 (volatile 중요)
    private volatile int currentEndpointIndex = 0; // 현재 사용 중인 엔드포인트 인덱스
    // 현재 연결된 RPC 엔드포인트 주소 반환 (디버깅/로깅용)
    @Getter
    private volatile String currentRpcEndpoint = ""; // 현재 사용중인 엔드포인트 주소 (로깅/디버깅용)

    @Autowired
    public Web3jConnectionManager(List<String> rpcEndpoints, OkHttpClient okHttpClient) {
        this.rpcEndpoints = rpcEndpoints;
        this.okHttpClient = okHttpClient;
    }

    @PostConstruct
    private void initializeConnection() {
        if (rpcEndpoints == null || rpcEndpoints.isEmpty()) {
            throw new IllegalStateException("설정된 RPC 엔드포인트가 없습니다.");
        }
        boolean connected = connect(); // 초기 연결 시도
        if (!connected) {
            logger.warn("초기화 중 모든 RPC 엔드포인트 연결에 실패했습니다. 애플리케이션 시작 후 첫 호출 시 재시도됩니다.");
        }
    }

    // 현재 활성화된 Web3j 인스턴스 반환 (직접 호출 최소화 권장, execute 사용)
    public Web3j getWeb3jInstance() {
        if (currentWeb3j == null) {
            logger.warn("Web3j 인스턴스가 null입니다. 재연결을 시도합니다.");
            if (!connect()) {
                throw new RuntimeException("Web3j 연결을 설정할 수 없습니다.");
            }
        }
        return currentWeb3j;
    }

    // 핵심 로직: 연결 시도 및 Web3j 인스턴스 교체
    private boolean connect() {
        // 이미 락을 소유한 스레드가 재진입하는 경우를 피하기 위해 tryLock 사용 고려 가능하나,
        // 현재 로직에서는 connect()가 재귀적으로 호출되지 않으므로 lock() 사용
        connectionLock.lock(); // 락 획득
        try {
            int startIndex = currentEndpointIndex;
            int listSize = rpcEndpoints.size();
            int attempts = 0;

            while (attempts < listSize) {
                String endpoint = rpcEndpoints.get(currentEndpointIndex);
                try {
                    logger.info("RPC 엔드포인트 연결 시도: {} (인덱스: {})", endpoint, currentEndpointIndex);
                    // 새 HttpService 와 Web3j 인스턴스 생성
                    Web3jService service = new HttpService(endpoint, okHttpClient, false);
                    Web3j web3j = Web3j.build(service);

                    // 간단한 연결 테스트 (블록 넘버 요청 등 다른 경량 호출로 대체 가능)
                    Web3ClientVersion version = web3j.web3ClientVersion().send(); // 타임아웃은 OkHttpClient 설정 따름
                    if (version.hasError()) {
                        throw new IOException("web3_clientVersion 호출 실패: " + version.getError().getMessage());
                    }
                    logger.info("성공적으로 연결됨: {} (클라이언트 버전: {})", endpoint, version.getWeb3ClientVersion());

                    // 이전 연결 종료 (리소스 정리)
                    shutdownPreviousConnection();

                    // 새 인스턴스로 교체 (volatile 쓰기)
                    this.currentWeb3j = web3j;
                    this.currentRpcEndpoint = endpoint; // 현재 연결된 주소 업데이트
                    return true; // 성공

                } catch (IOException e) {
                    logger.warn("RPC 엔드포인트 {} 연결 실패: {}", endpoint, e.getMessage());
                    // 다음 엔드포인트 시도
                    currentEndpointIndex = (currentEndpointIndex + 1) % listSize;
                    attempts++;
                }
            }
            // 모든 시도 실패
            logger.error("모든 RPC 엔드포인트 연결에 실패했습니다. 현재 활성 연결 없음.");
            shutdownPreviousConnection(); // 실패 시 기존 연결도 종료
            this.currentWeb3j = null;
            this.currentRpcEndpoint = "";
            return false;

        } finally {
            connectionLock.unlock(); // 반드시 락 해제
        }
    }

    // 이전 Web3j 인스턴스 종료 헬퍼 메서드
    private void shutdownPreviousConnection() {
        Web3j previousWeb3j = this.currentWeb3j; // 로컬 변수로 참조
        if (previousWeb3j != null) {
            try {
                logger.info("이전 Web3j 인스턴스 연결을 종료합니다 ({}).", this.currentRpcEndpoint);
                previousWeb3j.shutdown();
            } catch (Exception e) {
                logger.warn("이전 Web3j 인스턴스 종료 중 오류 발생", e);
            }
        }
    }

    // Web3j API 호출을 감싸는 메서드 (예외 처리 및 재연결/재시도)
    public <T> T execute(Web3jCallable<T> callable) throws Exception {
        // 재시도 횟수는 엔드포인트 수 + 1 (최초 시도 포함)
        int maxAttempts = rpcEndpoints.size() + 1;
        int attempt = 0;
        Exception lastException = null; // 마지막 발생 예외 저장

        while (attempt < maxAttempts) {
            attempt++;
            Web3j web3jInstance = this.currentWeb3j; // volatile 읽기

            if (web3jInstance == null) {
                logger.warn("Attempt {}: 현재 활성 Web3j 연결 없음. 재연결 시도.", attempt);
                if (connect()) { // connect() 호출은 내부적으로 락 사용
                    web3jInstance = this.currentWeb3j; // 재연결 성공 시 다시 읽기
                    if (web3jInstance == null) { // 드물지만 connect 성공 후 null일 경우 대비
                        logger.error("Attempt {}: 재연결 후에도 Web3j 인스턴스가 null입니다.", attempt);
                        lastException = new RuntimeException("재연결 후 Web3j 인스턴스 null");
                        continue; // 다음 시도
                    }
                } else {
                    logger.error("Attempt {}: 재연결 실패.", attempt);
                    lastException = new IOException("재연결 실패 후 작업 불가");
                    continue; // 다음 시도 (다른 노드가 있을 수 있음)
                }
            }

            try {
                // 실제 Web3j 작업 수행
                logger.debug("Attempt {}: Web3j 작업 실행 (Endpoint: {})", attempt, this.currentRpcEndpoint);
                return callable.call(web3jInstance);
            } catch (IOException e) { // 네트워크 관련 예외 감지
                lastException = e;
                logger.warn("Attempt {}: Web3j 작업 중 IOException 발생 (Endpoint: {}): {}. 재연결 시도.",
                        attempt, this.currentRpcEndpoint, e.getMessage());

                // 연결 문제로 간주되는 특정 예외들 확인 (더 구체적인 예외 추가 가능)
                if (e instanceof SocketTimeoutException || e instanceof ConnectException /* || 다른 연결 관련 예외 */) {
                    // 연결 실패 시 connect() 호출 (다음 노드 시도)
                    connect(); // 내부적으로 락 사용 및 currentWeb3j 업데이트 시도
                } else {
                    // 연결 문제가 아닌 다른 IOException은 재시도하지 않고 바로 던짐
                    logger.error("Attempt {}: 복구 불가능한 IOException 발생. 재시도 중단.", attempt, e);
                    throw e;
                }
            } catch (Exception ex) {
                // IOException 외 다른 모든 예외는 재시도 없이 바로 던짐 (예: 트랜잭션 실패, 잘못된 파라미터 등)
                logger.error("Attempt {}: Web3j 작업 중 예기치 않은 오류 발생. 재시도 중단.", attempt, ex);
                throw ex; // 즉시 예외 던짐
            }
        }
        // 모든 시도 후에도 실패한 경우
        logger.error("총 {}번의 시도 후에도 Web3j 작업 최종 실패.", maxAttempts);
        if (lastException != null) {
            throw lastException; // 마지막으로 발생한 예외 던짐
        } else {
            // 이 경우는 거의 발생하지 않아야 함
            throw new RuntimeException("알 수 없는 이유로 Web3j 작업 실행 실패");
        }
    }

    // Web3j 작업을 정의하기 위한 Functional Interface
    @FunctionalInterface
    public interface Web3jCallable<T> {
        T call(Web3j web3j) throws Exception;
    }
}
