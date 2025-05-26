package com.playus.searchservice.domain.post.service;

import com.playus.searchservice.IntegrationTestSupport;
import com.playus.searchservice.domain.common.adapter.TrendingKeywordAdapter;
import com.playus.searchservice.domain.post.dto.trending.TrendingKeywordResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class PostSearchServiceTest extends IntegrationTestSupport {

    private static final String KEY_NAME = "TRENDING-KEYWORD";

    @Autowired
    private PostSearchService postSearchService;

    @Autowired
    private RedisTemplate<String, String> trendingKeywordRedisTemplate;

    @Autowired
    private TrendingKeywordAdapter trendingKeywordAdapter;

    @AfterEach
    void tearDown() {
        trendingKeywordRedisTemplate.delete(KEY_NAME);
    }

    @DisplayName("10등까지의 실시간 검색어를 가져올 수 있다.")
    @Test
    void getTrendingKeyword() {

        // given
        trendingKeywordAdapter.addKeyword("spring"); // 1
        trendingKeywordAdapter.scoreKeyword("spring");
        trendingKeywordAdapter.scoreKeyword("spring");

        trendingKeywordAdapter.addKeyword("java");   // 2
        trendingKeywordAdapter.scoreKeyword("java");

        trendingKeywordAdapter.addKeyword("redis");  // 3

        // when
        List<TrendingKeywordResponse> result = postSearchService.getTrendingKeyword();

        // then
        assertThat(result).hasSize(3)
                .extracting("rank", "keyword")
                .containsExactlyInAnyOrder(
                        tuple(1, "spring"),
                        tuple(2, "java"),
                        tuple(3, "redis")
                );
    }

    @DisplayName("검색 기록이 없을 수 있다.")
    @Test
    void getTrendingKeyword_EMPYT() {
        // given

        // when
        List<TrendingKeywordResponse> result = postSearchService.getTrendingKeyword();

        // then
        assertThat(result).isEmpty();
    }

}
