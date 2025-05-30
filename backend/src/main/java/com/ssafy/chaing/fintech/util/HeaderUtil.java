package com.ssafy.chaing.fintech.util;

import com.ssafy.chaing.fintech.service.common.HeaderWithUserKeyDTO;
import com.ssafy.chaing.fintech.service.common.HeaderWithoutUserKeyDTO;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HeaderUtil {

    @Value("${ssafy.fintech.api-key}")
    private String apiKey;
    @Value("${ssafy.fintech.user-key}")
    private String userKey;

    public HeaderWithUserKeyDTO createFintechHeaderWithUserKey(String apiName, String apiServiceCode) {

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

        String transmissionDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String transmissionTime = now.format(DateTimeFormatter.ofPattern("HHmmss"));

        String institutionTransactionUniqueNo = generateInstitutionCode(transmissionDate, transmissionTime);

        return new HeaderWithUserKeyDTO(
                apiName,
                transmissionDate,
                transmissionTime,
                "00100",
                "001",
                apiServiceCode,
                institutionTransactionUniqueNo,
                apiKey,
                userKey
        );
    }

    public HeaderWithoutUserKeyDTO createFintechHeader(String apiName, String apiServiceCode) {
        Date now = new Date();
        String transmissionDate = new SimpleDateFormat("yyyyMMdd").format(now);
        String transmissionTime = new SimpleDateFormat("HHmmss").format(now);

        String institutionTransactionUniqueNo = generateInstitutionCode(transmissionDate, transmissionTime);

        return new HeaderWithoutUserKeyDTO(
                apiName,
                transmissionDate,
                transmissionTime,
                "00100",
                "001",
                apiServiceCode,
                institutionTransactionUniqueNo,
                apiKey
        );
    }


    private String generateInstitutionCode(String transmissionDate, String transmissionTime) {
        // 랜덤 6자리 숫자 생성
        Random random = new Random();
        String randomDigits = String.format("%06d", random.nextInt(1_000_000));

        return transmissionDate + transmissionTime + randomDigits;
    }
}
