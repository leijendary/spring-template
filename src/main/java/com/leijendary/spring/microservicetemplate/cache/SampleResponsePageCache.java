package com.leijendary.spring.microservicetemplate.cache;

import com.leijendary.spring.microservicetemplate.data.AppPage;
import com.leijendary.spring.microservicetemplate.data.response.SampleResponse;
import org.modelmapper.TypeToken;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class SampleResponsePageCache extends AppCache<AppPage<SampleResponse>> {

    public SampleResponsePageCache(CacheManager cacheManager) {
        super(cacheManager);
    }

    @Override
    public Class<AppPage<SampleResponse>> type() {
        return new TypeToken<AppPage<SampleResponse>>(){}.getRawType();
    }

    @Override
    public String cacheName() {
        return type().getSimpleName() + splitter() + SampleResponse.class.getSimpleName();
    }
}
