package com.ssafy.chaing.common.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GPTUtilTest {

    @Autowired
    private GPTUtil gptUtil;

//    @Test
//    void testClassifyLifeRuleContent() {
//        // given
//        String content = "나는 매일 아침 러닝을 합니다.";
//
//        // when
//        String result = gptUtil.classifyLifeRuleContent(content);
//
//        // then
//        System.out.println("GPT 결과: " + result);
//        assertThat(result).isNotBlank();
//    }

    @Test
    void testClassifyDutyContent() {
        // given
        String content = "화장실 청소를 매주 금요일에 합니다.";

        // when
        String result = gptUtil.classifyDutyContent(content);

        // then
        System.out.println("GPT 결과: " + result);
        assertThat(result).isNotBlank();
    }
}