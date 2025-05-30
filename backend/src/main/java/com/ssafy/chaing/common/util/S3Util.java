package com.ssafy.chaing.common.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.chaing.common.exception.ExceptionCode;
import com.ssafy.chaing.common.exception.ServerException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3Util {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    /**
     * 파일을 S3에 업로드하고, 해당 파일의 URL을 반환합니다.
     * @param file MultipartFile 형식의 업로드 파일
     * @return S3에 저장된 파일의 URL
     */
    public String uploadFile(MultipartFile file, Long entityId) {
        String fileName = entityId.toString() + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();  // 고유한 파일 이름 생성
        try {
            amazonS3.putObject(
                    new PutObjectRequest(bucketName,
                            fileName,
                            file.getInputStream(),
                            null)
                    );
        } catch (IOException e) {
            throw new ServerException(ExceptionCode.S3_UPLOAD_FAILED);
        }
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    /**
     * 주어진 바이트 배열을 PDF 파일로 S3에 업로드하고 URL을 반환합니다. (AWS SDK v1 사용)
     *
     * @param pdfBytes     업로드할 PDF 데이터
     * @param filePrefix   S3 키(파일 경로)의 접두사 (예: "portfolios/contracts")
     * @param baseFileName 파일 이름의 기본 부분 (예: "contract-123")
     * @return 업로드된 파일의 S3 URL
     * @throws SdkClientException 업로드 중 클라이언트 측 오류 발생 시 (네트워크 등)
     * @throws AmazonServiceException 업로드 중 AWS 서비스 측 오류 발생 시
     */
    public String uploadPdf(byte[] pdfBytes, String filePrefix, String baseFileName)
            throws SdkClientException, AmazonServiceException {

        String keyName = generateUniqueKey(filePrefix, baseFileName);

        // 1. InputStream 생성

        // 2. ObjectMetadata 설정 (Content-Type, Content-Length 등)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/pdf");
        metadata.setContentLength(pdfBytes.length);

        try(InputStream inputStream = new ByteArrayInputStream(pdfBytes)) {
            // 3. PutObjectRequest 생성
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, inputStream, metadata);

            // 4. S3에 업로드
            amazonS3.putObject(putObjectRequest);
            log.info("Successfully uploaded PDF to S3 bucket '{}' with key '{}' using SDK v1", bucketName, keyName);

            // 5. 업로드된 파일의 URL 가져오기
            URL url = amazonS3.getUrl(bucketName, keyName);
            return url.toString();

        } catch (AmazonServiceException e) {
            // AWS 서비스 오류 (예: 권한 없음, 버킷 없음)
            log.error("Error uploading PDF to S3 (Service Exception): {}", e.getErrorMessage(), e);
            throw e; // 예외를 다시 던져 상위 서비스에서 처리하도록 함
        } catch (SdkClientException e) {
            // 클라이언트 오류 (예: 네트워크 연결 불가)
            log.error("Error uploading PDF to S3 (Client Exception): {}", e.getMessage(), e);
            throw e; // 예외를 다시 던져 상위 서비스에서 처리하도록 함
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * S3 내에서 고유한 파일 키(경로 포함)를 생성합니다.
     * 예: portfolios/contracts/contract-123-2025-03-27T12-34-56Z-uuid.pdf
     */
    private String generateUniqueKey(String prefix, String baseName) {
        String timestamp = Instant.now().toString().replace(":", "-");
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String fullPrefix = (prefix == null || prefix.isEmpty()) ? "" :
                (prefix.endsWith("/") ? prefix : prefix + "/");

        return String.format("%s%s-%s-%s.pdf",
                fullPrefix,
                baseName,
                timestamp,
                uniqueId);
    }

}