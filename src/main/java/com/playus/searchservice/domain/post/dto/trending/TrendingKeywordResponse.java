package com.playus.searchservice.domain.post.dto.trending;

import lombok.Builder;

@Builder
public record TrendingKeywordResponse(
      int rank,
      String keyword
) {
    public static TrendingKeywordResponse of (int rank, String keyword) {
        return TrendingKeywordResponse.builder()
                .rank(rank)
                .keyword(keyword)
                .build();
    }
}
