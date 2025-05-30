package com.playus.searchservice.domain.post.dto.search;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SearchRequest(

        @NotBlank(message = "검색어는 필수입니다!")
        String query
) {
}
