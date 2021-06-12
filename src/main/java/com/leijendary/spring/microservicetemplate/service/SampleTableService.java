package com.leijendary.spring.microservicetemplate.service;

import com.leijendary.spring.microservicetemplate.data.request.QueryRequest;
import com.leijendary.spring.microservicetemplate.data.request.v1.SampleRequestV1;
import com.leijendary.spring.microservicetemplate.data.response.v1.SampleResponseV1;
import com.leijendary.spring.microservicetemplate.exception.ResourceNotFoundException;
import com.leijendary.spring.microservicetemplate.factory.SampleFactory;
import com.leijendary.spring.microservicetemplate.repository.SampleTableRepository;
import com.leijendary.spring.microservicetemplate.specification.SampleListSpecification;
import com.leijendary.spring.microservicetemplate.validator.v1.SampleRequestV1Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.leijendary.spring.microservicetemplate.factory.SampleFactory.toResponseV1;

@Service
@RequiredArgsConstructor
public class SampleTableService extends AbstractService {

    private static final String RESOURCE_NAME = "Sample Table";
    private static final String PAGE_CACHE_V1 = "SampleResponsePageV1";
    private static final String CACHE_V1 = "SampleResponseV1";

    private final SampleRequestV1Validator sampleRequestV1Validator;
    private final SampleTableRepository sampleTableRepository;

    @Cacheable(value = PAGE_CACHE_V1, key = "#queryRequest.toString() + '|' + #pageable.toString()")
    public Page<SampleResponseV1> list(final QueryRequest queryRequest, final Pageable pageable) {
        final var specification = SampleListSpecification.builder()
                .column1(queryRequest.getQuery())
                .build();

        return sampleTableRepository.findAll(specification, pageable)
                .map(SampleFactory::toResponseV1);
    }

    @Caching(
            evict = @CacheEvict(value = PAGE_CACHE_V1, allEntries = true),
            put = @CachePut(value = CACHE_V1, key = "#result.id"))
    public SampleResponseV1 create(final SampleRequestV1 sampleRequest) {
        validate(sampleRequestV1Validator, sampleRequest, SampleRequestV1.class);

        final var sampleTable = SampleFactory.of(sampleRequest);

        sampleTableRepository.save(sampleTable);

        return toResponseV1(sampleTable);
    }

    @Caching(
            evict = @CacheEvict(value = PAGE_CACHE_V1, allEntries = true),
            put = @CachePut(value = CACHE_V1, key = "#result.id"))
    public SampleResponseV1 update(final long id, final SampleRequestV1 sampleRequest) {
        final var sampleTable = sampleTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));

        validate(sampleRequestV1Validator, sampleRequest, SampleRequestV1.class);

        SampleFactory.map(sampleRequest, sampleTable);

        sampleTableRepository.save(sampleTable);

        return toResponseV1(sampleTable);
    }

    @Cacheable(value = CACHE_V1, key = "#id")
    public SampleResponseV1 get(final long id) {
        return sampleTableRepository
                .findById(id)
                .map(SampleFactory::toResponseV1)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
    }

    @Caching(evict = {
            @CacheEvict(value = PAGE_CACHE_V1, allEntries = true),
            @CacheEvict(value = CACHE_V1, key = "#id") })
    public void delete(final long id) {
        final var sampleTable = sampleTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));

        sampleTableRepository.delete(sampleTable);
    }
}
