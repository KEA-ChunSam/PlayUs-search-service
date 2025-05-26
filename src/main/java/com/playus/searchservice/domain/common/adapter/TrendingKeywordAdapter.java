package com.playus.searchservice.domain.common.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class TrendingKeywordAdapter {

    private static final String KEY_NAME = "TRENDING-KEYWORD";
    private final RedisTemplate<String, String> trendingKeywordRedisTemplate;

    public Boolean addKeyword(String keyword) {
        return trendingKeywordRedisTemplate.opsForZSet().add(KEY_NAME, keyword, 1);
    }

    public Double scoreKeyword (String keyword) {
        return trendingKeywordRedisTemplate.opsForZSet().incrementScore(KEY_NAME, keyword, 1);
    }

    public Boolean existsKeyword(String keyword) {
        Double score = trendingKeywordRedisTemplate.opsForZSet().score(KEY_NAME, keyword);
        return score != null;
    }

    public Set<ZSetOperations.TypedTuple<String>> getTop10Keywords() {
        return trendingKeywordRedisTemplate.opsForZSet().reverseRangeWithScores(KEY_NAME, 0, 9);
    }
}
