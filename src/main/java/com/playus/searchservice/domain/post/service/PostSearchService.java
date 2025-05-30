package com.playus.searchservice.domain.post.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.playus.searchservice.domain.common.adapter.TrendingKeywordAdapter;
import com.playus.searchservice.domain.common.feign.client.UserFeignClient;
import com.playus.searchservice.domain.common.feign.response.PartyWriterInfoFeignResponse;
import com.playus.searchservice.domain.post.document.PostDocument;
import com.playus.searchservice.domain.post.dto.search.SearchRequest;
import com.playus.searchservice.domain.post.dto.search.SearchResponse;
import com.playus.searchservice.domain.post.dto.trending.TrendingKeywordResponse;
import com.playus.searchservice.domain.post.vo.PostSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostSearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final TrendingKeywordAdapter trendingKeywordAdapter;
    private final UserFeignClient userFeignClient;

    public SearchResponse search(SearchRequest request) {

        String query = request.query();

        Query titleMatchQuery = MatchQuery.of(m -> m
                        .query(request.query())
                        .field("title")
                        .fuzziness("AUTO"))
                ._toQuery();

        List<Query> filters = new ArrayList<>();

        createSoftAndSecretFilter(filters);

        Query boolQuery = BoolQuery.of(b -> b
                        .must(titleMatchQuery)
                        .filter(filters))
                ._toQuery();


        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(boolQuery)
                .build();

        SearchHits<PostDocument> searchResult = elasticsearchOperations.search(nativeQuery, PostDocument.class);

        increaseTrendingKeyword(query);

        List<PostSearchResult> resultList = searchResult.getSearchHits().stream()
                .map(result -> result.getContent().toPostSearchResult())
                .toList();

        updateWriterInfo(resultList);

        return SearchResponse.of(resultList.size(), resultList);
    }

    public List<TrendingKeywordResponse> getTrendingKeyword() {
        Set<ZSetOperations.TypedTuple<String>> topKeywords =
                trendingKeywordAdapter.getTop10Keywords();

        if (topKeywords == null) {
            return Collections.emptyList();
        }

        AtomicInteger rank = new AtomicInteger(1);
        return topKeywords.stream()
                .map(ZSetOperations.TypedTuple::getValue)
                .filter(Objects::nonNull)
                .map(keyword -> TrendingKeywordResponse.of(rank.getAndIncrement(), keyword))
                .toList();
    }

    private void updateWriterInfo(List<PostSearchResult> resultList) {
        List<Long> writerIdList = resultList.stream()
                .map(PostSearchResult::getWriterId)
                .toList();

        List<PartyWriterInfoFeignResponse> writerInfoList = userFeignClient.getWriterInfo(writerIdList);

        Map<Long, PartyWriterInfoFeignResponse> writerInfoMap = writerInfoList.stream()
                .collect(Collectors.toMap(PartyWriterInfoFeignResponse::id, Function.identity(), (a, b) -> a)); // 중복 ID 있을 경우 첫 번째 유지

        for (PostSearchResult post : resultList) {
            PartyWriterInfoFeignResponse writerInfo = writerInfoMap.get(post.getWriterId());
            if (writerInfo != null) {
                post.updateWriterName(writerInfo.writerName());
            }
        }
    }

    private static void createSoftAndSecretFilter(List<Query> filters) {
        Query notDeletedPostFilter = TermQuery.of(t -> t
                .field("activated")
                .value(true)
        )._toQuery();
        filters.add(notDeletedPostFilter);

        Query notSecretPostFilter = TermQuery.of(t -> t
                .field("is_secret")
                .value(false)
        )._toQuery();
        filters.add(notSecretPostFilter);
    }

    private void increaseTrendingKeyword(String query) {
        if (trendingKeywordAdapter.existsKeyword(query)) {
            trendingKeywordAdapter.scoreKeyword(query);
        } else {
            trendingKeywordAdapter.addKeyword(query);
        }
    }
}
