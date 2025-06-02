package com.playus.searchservice.domain.post.specification;

import com.playus.searchservice.domain.post.dto.search.SearchRequest;
import com.playus.searchservice.domain.post.dto.search.SearchResponse;
import com.playus.searchservice.domain.post.dto.trending.TrendingKeywordResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "검색 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "검색 응답 예시",
                                    value = """
                                            {
                                                 "resultSize": 1,
                                                 "searchResultList": [
                                                     {
                                                         "postId": 71,
                                                         "writerId": 2,
                                                         "writerName": null,
                                                         "title": "LG가 좋아요",
                                                         "thumbnailUrl": "post.jpg",
                                                         "teamTag": "DOOSAN_BEARS",
                                                         "createdAt": "11:42"
                                                     }
                                                 ]
                                             }
                                            """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "400", description = "검색 시도 시 검색어가 비어 있을 때 발생",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "code": 400,
                                              "status": "BAD_REQUEST",
                                              "message": "검색어는 필수입니다!"
                                            }
                                            """
                            )
                    )
            )
    })
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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "인기 검색어 조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "인기 검색어 응답 예시",
                                    value = """
                                            [
                                                  {
                                                      "rank": 1,
                                                      "keyword": "LG"
                                                  },
                                                  {
                                                      "rank": 2,
                                                      "keyword": "NC"
                                                  },
                                                  {
                                                      "rank": 3,
                                                      "keyword": "NC가"
                                                  },
                                                  {
                                                      "rank": 4,
                                                      "keyword": "삼성"
                                                  },
                                                  {
                                                      "rank": 5,
                                                      "keyword": "사과"
                                                  },
                                                  {
                                                      "rank": 6,
                                                      "keyword": "lg"
                                                  },
                                                  {
                                                      "rank": 7,
                                                      "keyword": "테스트"
                                                  },
                                                  {
                                                      "rank": 8,
                                                      "keyword": "test"
                                                  },
                                                  {
                                                      "rank": 9,
                                                      "keyword": "nc"
                                                  },
                                                  {
                                                      "rank": 10,
                                                      "keyword": "text"
                                                  }
                                              ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<List<TrendingKeywordResponse>> getTrendingKeyword();
}
