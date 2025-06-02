package com.playus.searchservice.global.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndexNameProvider {

    @Value("${spring.elasticsearch.index}")
    private String indexName;


    public String indexName() {
        return indexName;
    }
}
