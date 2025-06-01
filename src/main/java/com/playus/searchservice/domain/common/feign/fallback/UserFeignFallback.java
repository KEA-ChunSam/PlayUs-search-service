package com.playus.searchservice.domain.common.feign.fallback;

import com.playus.searchservice.domain.common.feign.client.UserFeignClient;
import com.playus.searchservice.domain.common.feign.request.TokenValidationRequest;
import com.playus.searchservice.domain.common.feign.response.PartyWriterInfoFeignResponse;
import com.playus.searchservice.domain.common.feign.response.TokenValidationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserFeignFallback implements UserFeignClient {

    @Override
    public List<PartyWriterInfoFeignResponse> getWriterInfo(List<Long> writerIdList) {
        log.error("503 happened in UserFeignClient at fetching writer data!!!");
        return List.of(PartyWriterInfoFeignResponse.withServiceUnavailable());
    }

    @Override
    public ResponseEntity<TokenValidationResponse> checkBlackList(TokenValidationRequest req) {
        log.error("503 happened in UserFeignClient at validating token!!!");
        return ResponseEntity.internalServerError().body(TokenValidationResponse.of(true));
    }
}
