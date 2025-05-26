package com.playus.searchservice.domain.post.controller;

import com.playus.searchservice.domain.post.dto.search.SearchRequest;
import com.playus.searchservice.domain.post.dto.search.SearchResponse;
import com.playus.searchservice.domain.post.service.PostSearchService;
import com.playus.searchservice.domain.post.specification.PostSearchControllerSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class PostSearchController implements PostSearchControllerSpecification {

    private final PostSearchService postSearchService;

    @Override
    @PostMapping("/posts")
    public ResponseEntity<SearchResponse> search(@Valid SearchRequest request) {
        SearchResponse response = postSearchService.search(request);
        return ResponseEntity.ok().body(response);
    }
}
