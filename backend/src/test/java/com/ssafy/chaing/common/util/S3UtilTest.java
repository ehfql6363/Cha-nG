package com.ssafy.chaing.common.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
class S3UtilTest {
    @Autowired
    private S3Util s3Util;

    @Test
    void s3업로드테스트() throws Exception {
        // 테스트용 이미지 경로 설정 (resources 폴더에 임시 이미지 두고 테스트 가능)
        String filePath = "src/test/resources/test-image.jpg";
        FileInputStream fis = new FileInputStream(filePath);
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test-image.png",
                "image/png",
                fis
        );

        String uploadedUrl = s3Util.uploadFile(multipartFile, 123L);
        assertNotNull(uploadedUrl, "업로드된 URL이 null입니다.");
        assertFalse(uploadedUrl.isEmpty(), "업로드된 URL이 비어 있습니다.");
    }
}