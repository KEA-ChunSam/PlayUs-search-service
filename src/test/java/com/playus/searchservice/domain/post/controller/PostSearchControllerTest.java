package com.playus.searchservice.domain.post.controller;

import com.playus.searchservice.ControllerTestSupport;
import com.playus.searchservice.domain.common.security.CustomOAuth2User;
import com.playus.searchservice.domain.common.security.Role;
import com.playus.searchservice.domain.post.dto.search.SearchRequest;
import com.playus.searchservice.domain.post.dto.search.SearchResponse;
import com.playus.searchservice.domain.post.enums.TeamTag;
import com.playus.searchservice.domain.post.vo.PostSearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostSearchControllerTest extends ControllerTestSupport {


    private UsernamePasswordAuthenticationToken token;

    @BeforeEach
    void setUp() {
        long userId = 1L;

        // 더미 OAuth2 사용자
        CustomOAuth2User principal = Mockito.mock(CustomOAuth2User.class);
        when(principal.getName()).thenReturn(Long.toString(userId));

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(Role.USER.name()));
        doReturn(authorities).when(principal).getAuthorities();

        token = new UsernamePasswordAuthenticationToken(
                principal, null, authorities
        );
    }

    @DisplayName("커뮤니티 게시글을 검색할 수 있다.")
    @Test
    void search() throws Exception {
        // given
        String query = "테스트";
        List<PostSearchResult> searchResultList = List.of(
                PostSearchResult.of(1L, "kim", "title", "http://thumbnailUrl", TeamTag.DOOSAN_BEARS, LocalDateTime.of(2025, 5, 25, 10, 0, 0))
        );
        SearchResponse response = SearchResponse.of(1, searchResultList);
        given(postSearchService.search(any(SearchRequest.class))).willReturn(response);

        // when // then
        mockMvc.perform(get("/search/posts")
                        .param("query", query)
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultSize").value(1))
                .andExpect(jsonPath("$.searchResultList[0].postId").value(1L))
                .andExpect(jsonPath("$.searchResultList[0].writerName").value("kim"))
                .andExpect(jsonPath("$.searchResultList[0].title").value("title"))
                .andExpect(jsonPath("$.searchResultList[0].thumbnailUrl").value("http://thumbnailUrl"))
                .andExpect(jsonPath("$.searchResultList[0].teamTag").value("DOOSAN_BEARS"))
                .andExpect(jsonPath("$.searchResultList[0].createdAt").value("10:00"));
    }

    @DisplayName("게시글 검색 시 검색어는 필수이다.")
    @NullAndEmptySource
    @ParameterizedTest
    void search_EMPTY_QUERY(String emptyQuery) throws Exception {
        // given

        // when // then
        mockMvc.perform(get("/search/posts")
                        .param("query", emptyQuery)
                        .contentType(APPLICATION_JSON)
                        .with(authentication(token)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("검색어는 필수입니다!"));
    }
}

