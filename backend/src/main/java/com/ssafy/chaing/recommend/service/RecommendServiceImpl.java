package com.ssafy.chaing.recommend.service;

import com.ssafy.chaing.common.util.GPTUtil;
import com.ssafy.chaing.recommend.Request.RecommendRequest;
import com.ssafy.chaing.recommend.response.RecommendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService{

    private final GPTUtil gptUtil;

    @Override
    public RecommendResponse recommendLifeRuleCategory(RecommendRequest request) {
        String lifeRuleContent = gptUtil.classifyLifeRuleContent(request.getContent());
        return new RecommendResponse(lifeRuleContent);
    }

    @Override
    public RecommendResponse recommendDutyCategory(RecommendRequest request) {
        String lifeRuleContent = gptUtil.classifyDutyContent(request.getContent());
        return new RecommendResponse(lifeRuleContent);
    }
}
