package com.ssafy.chaing.recommend.service;

import com.ssafy.chaing.recommend.Request.RecommendRequest;
import com.ssafy.chaing.recommend.response.RecommendResponse;

public interface RecommendService {

    RecommendResponse recommendLifeRuleCategory(RecommendRequest request);

    RecommendResponse recommendDutyCategory(RecommendRequest request);

}
