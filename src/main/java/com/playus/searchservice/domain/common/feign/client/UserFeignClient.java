package com.playus.searchservice.domain.common.feign.client;

import com.playus.searchservice.domain.common.feign.fallback.UserFeignFallback;
import com.playus.searchservice.domain.common.feign.request.TokenValidationRequest;
import com.playus.searchservice.domain.common.feign.response.PartyWriterInfoFeignResponse;
import com.playus.searchservice.domain.common.feign.response.TokenValidationResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "userFeignClient", url = "${feign.user.url}", path = "/user/api", fallback = UserFeignFallback.class)
@CircuitBreaker(name = "circuit")
public interface UserFeignClient {

    @PostMapping("/writers")
    List<PartyWriterInfoFeignResponse> getWriterInfo(@RequestBody List<Long> writerIdList);

    @PostMapping("/token/blacklist-check")
    ResponseEntity<TokenValidationResponse> checkBlackList(@RequestBody TokenValidationRequest req);
}
