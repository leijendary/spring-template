package com.leijendary.spring.microservicetemplate.cache;

import com.leijendary.spring.microservicetemplate.data.response.SampleResponse;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;


@Component
public class SampleResponseCache extends AppCache<SampleResponse> {

    public SampleResponseCache(CacheManager cacheManager) {
        super(cacheManager);
    }

    @Override
    public Class<SampleResponse> type() {
        return SampleResponse.class;
    }
}
