package com.playus.searchservice.domain.post.controller;

import com.playus.searchservice.domain.post.dto.search.SearchRequest;
import com.playus.searchservice.domain.post.dto.search.SearchResponse;
import com.playus.searchservice.domain.post.dto.trending.TrendingKeywordResponse;
import com.playus.searchservice.domain.post.service.PostSearchService;
import com.playus.searchservice.domain.post.specification.PostSearchControllerSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class PostSearchController implements PostSearchControllerSpecification {

    private final PostSearchService postSearchService;

    @Override
    @GetMapping("/posts")
    public ResponseEntity<SearchResponse> search(@Valid SearchRequest request) {
        SearchResponse response = postSearchService.search(request);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @GetMapping("/trending")
    public ResponseEntity<List<TrendingKeywordResponse>> getTrendingKeyword() {
        List<TrendingKeywordResponse> response = postSearchService.getTrendingKeyword();
        return ResponseEntity.ok().body(response);
    }
}
