package com.playus.searchservice.domain.common.adapter;

import com.playus.searchservice.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TrendingKeywordAdapterTest extends IntegrationTestSupport {

    private static final String KEY_NAME = "TRENDING-KEYWORD";

    @Autowired
    private TrendingKeywordAdapter trendingKeywordAdapter;

    @Autowired
    private RedisTemplate<String, String> trendingKeywordRedisTemplate;

    @AfterEach
    void tearDown() {
        trendingKeywordRedisTemplate.delete(KEY_NAME);
    }

    @Test
    @DisplayName("새로운 키워드를 추가할 수 있다")
    void addKeyword_success() {
        // given
        String keyword = "springboot";

        // when
        Boolean result = trendingKeywordAdapter.addKeyword(keyword);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("기존 키워드의 점수를 증가시킬 수 있다")
    void scoreKeyword_success() {
        // given
        String keyword = "springboot";
        trendingKeywordAdapter.addKeyword(keyword);

        // when
        Double score = trendingKeywordAdapter.scoreKeyword(keyword);

        // then
        assertThat(score).isEqualTo(2.0);
    }

    @DisplayName("특정 키워드가 검색된 적 있는지 확인할 수 있다.")
    @Test
    void existsKeyword() {
        // given
        trendingKeywordAdapter.addKeyword("spring");

        // when
        Boolean result = trendingKeywordAdapter.existsKeyword("spring");

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("특정 키워드가 검색된 적 없을 수 있다.")
    @Test
    void existsKeyword_NOT_EXISTS() {
      // given

        // when
        Boolean result = trendingKeywordAdapter.existsKeyword("spring");

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("상위 10개의 키워드를 순위와 함께 조회할 수 있다")
    void getTop10Keywords_success() {
        // given
        trendingKeywordAdapter.addKeyword("spring"); // 1
        trendingKeywordAdapter.addKeyword("java");   // 1
        trendingKeywordAdapter.addKeyword("redis");  // 1

        trendingKeywordAdapter.scoreKeyword("spring"); // 2
        trendingKeywordAdapter.scoreKeyword("spring"); // 3

        // when
        Set<ZSetOperations.TypedTuple<String>> topKeywords = trendingKeywordAdapter.getTop10Keywords();

        // then
        assertThat(topKeywords).hasSize(3);

        List<ZSetOperations.TypedTuple<String>> resultList = new ArrayList<>(topKeywords);

        assertThat(resultList.get(0).getValue()).isEqualTo("spring");
        assertThat(resultList.get(0).getScore()).isEqualTo(3.0);

        // 나머지 키워드들도 존재하는지만 확인
        List<String> keywords = resultList.stream()
                .map(ZSetOperations.TypedTuple::getValue)
                .toList();

        assertThat(keywords).contains("java", "redis");
    }
}
