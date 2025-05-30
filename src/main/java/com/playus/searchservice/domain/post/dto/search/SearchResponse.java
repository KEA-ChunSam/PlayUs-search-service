package com.playus.searchservice.domain.post.dto.search;

import com.playus.searchservice.domain.post.vo.PostSearchResult;
import lombok.Builder;

import java.util.List;

@Builder
public record SearchResponse(
        int resultSize,
        List<PostSearchResult> searchResultList
) {

    public static SearchResponse of (int resultSize, List<PostSearchResult> searchResultList) {
        return SearchResponse.builder()
                .resultSize(resultSize)
                .searchResultList(searchResultList)
                .build();
    }
}
