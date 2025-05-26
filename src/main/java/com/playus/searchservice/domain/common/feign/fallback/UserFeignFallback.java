package com.playus.searchservice.domain.common.feign.fallback;

import com.playus.searchservice.domain.common.feign.client.UserFeignClient;
import com.playus.searchservice.domain.common.feign.response.PartyWriterInfoFeignResponse;
import lombok.extern.slf4j.Slf4j;
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
}
