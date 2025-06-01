package com.playus.searchservice.domain.post.specification;

import com.playus.searchservice.domain.post.dto.search.SearchRequest;
import com.playus.searchservice.domain.post.dto.search.SearchResponse;
import com.playus.searchservice.domain.post.dto.trending.TrendingKeywordResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface PostSearchControllerSpecification {


    @Tag(name = "Get", description = "커뮤니티 글 검색 API")
    @Operation(
            summary = "커뮤니티 글 검색",
            description = "커뮤니티 글을 검색합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    ),
                    @Parameter(
                            name = "query",
                            description = "검색어",
                            in = ParameterIn.QUERY,
                            required = true
                    )
            }
    )
    ResponseEntity<SearchResponse> search(@Valid @Parameter(hidden = true, description = "검색어")
                                          SearchRequest request);


    @Tag(name = "Get", description = "커뮤니티 인기 검색어 조회 API")
    @Operation(
            summary = "커뮤니티 인기 검색어 조회",
            description = "커뮤니티 인기 검색어를 조회합니다.",
            security = @SecurityRequirement(name = "Access"),
            parameters = {
                    @Parameter(
                            name = "Access",
                            description = "JWT Access Token (쿠키)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    )
            }
    )
    ResponseEntity<List<TrendingKeywordResponse>> getTrendingKeyword();
}
