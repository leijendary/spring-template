package com.leijendary.spring.microservicetemplate.flow;

import com.leijendary.spring.microservicetemplate.data.request.QueryRequest;
import com.leijendary.spring.microservicetemplate.data.request.v1.SampleRequestV1;
import com.leijendary.spring.microservicetemplate.data.response.v1.SampleResponseV1;
import com.leijendary.spring.microservicetemplate.factory.SampleDataFactory;
import com.leijendary.spring.microservicetemplate.factory.SampleFactory;
import com.leijendary.spring.microservicetemplate.service.SampleTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static com.leijendary.spring.microservicetemplate.factory.SampleFactory.toResponseV1;

@Component
@RequiredArgsConstructor
public class SampleFlow {

    private static final String PAGE_CACHE_V1 = "SampleResponsePageV1";
    private static final String CACHE_V1 = "SampleResponseV1";

    private final SampleTableService sampleTableService;

    @Cacheable(value = PAGE_CACHE_V1, key = "#queryRequest.toString() + '|' + #pageable.toString()")
    public Page<SampleResponseV1> listV1(final QueryRequest queryRequest, final Pageable pageable) {
        return sampleTableService.list(queryRequest, pageable)
                .map(SampleFactory::toResponseV1);
    }

    @Caching(
            evict = @CacheEvict(value = PAGE_CACHE_V1, allEntries = true),
            put = @CachePut(value = CACHE_V1, key = "#result.id"))
    @Transactional
    public SampleResponseV1 createV1(final SampleRequestV1 request) {
        final var sampleData = SampleDataFactory.of(request);
        final var sampleTable = sampleTableService.create(sampleData);

        return toResponseV1(sampleTable);
    }

    @Cacheable(value = CACHE_V1, key = "#id")
    @Transactional
    public SampleResponseV1 getV1(final long id) {
        final var sampleTable = sampleTableService.get(id);

        return toResponseV1(sampleTable);
    }

    @Caching(evict = {
            @CacheEvict(value = PAGE_CACHE_V1, allEntries = true),
            @CacheEvict(value = CACHE_V1, key = "#id") })
    @Transactional
    public SampleResponseV1 updateV1(final long id, final SampleRequestV1 request) {
        final var sampleData = SampleDataFactory.of(request);
        final var sampleTable = sampleTableService.update(id, sampleData);

        return toResponseV1(sampleTable);
    }

    @Caching(evict = {
            @CacheEvict(value = PAGE_CACHE_V1, allEntries = true),
            @CacheEvict(value = CACHE_V1, key = "#id") })
    @Transactional
    public void deleteV1(final long id) {
        sampleTableService.delete(id);
    }
}
