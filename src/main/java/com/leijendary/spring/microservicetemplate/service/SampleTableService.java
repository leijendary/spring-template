package com.leijendary.spring.microservicetemplate.service;

import com.leijendary.spring.microservicetemplate.data.AppPage;
import com.leijendary.spring.microservicetemplate.data.request.QueryRequest;
import com.leijendary.spring.microservicetemplate.data.request.v1.SampleRequestV1;
import com.leijendary.spring.microservicetemplate.data.response.v1.SampleResponseV1;
import com.leijendary.spring.microservicetemplate.exception.ResourceNotFoundException;
import com.leijendary.spring.microservicetemplate.factory.AppPageFactory;
import com.leijendary.spring.microservicetemplate.factory.SampleFactory;
import com.leijendary.spring.microservicetemplate.repository.SampleTableRepository;
import com.leijendary.spring.microservicetemplate.specification.SampleListSpecification;
import com.leijendary.spring.microservicetemplate.validator.v1.SampleRequestV1Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.leijendary.spring.microservicetemplate.factory.SampleFactory.toResponseV1;

@Service
@RequiredArgsConstructor
public class SampleTableService extends AbstractService {

    private final SampleRequestV1Validator sampleRequestV1Validator;
    private final SampleTableRepository sampleTableRepository;

    @Cacheable(value = "SampleResponsePageV1", key = "#queryRequest.toString() + '|' + #pageable.toString()")
    public AppPage<SampleResponseV1> list(final QueryRequest queryRequest, final Pageable pageable) {
        final var specification = SampleListSpecification.builder()
                .column1(queryRequest.getQuery())
                .build();
        final var page = sampleTableRepository.findAll(specification, pageable)
                .map(SampleFactory::toResponseV1);

        return AppPageFactory.of(page);
    }

    @CachePut(value = "SampleResponseV1", key = "#result.id")
    public SampleResponseV1 create(final SampleRequestV1 sampleRequest) {
        validate(sampleRequestV1Validator, sampleRequest, SampleRequestV1.class);

        final var sampleTable = SampleFactory.of(sampleRequest);

        sampleTableRepository.save(sampleTable);

        return toResponseV1(sampleTable);
    }

    @CachePut(value = "SampleResponseV1", key = "#id")
    public SampleResponseV1 update(final int id, final SampleRequestV1 sampleRequest) {
        final var sampleTable = sampleTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sample Table", id));

        validate(sampleRequestV1Validator, sampleRequest, SampleRequestV1.class);

        SampleFactory.map(sampleRequest, sampleTable);

        sampleTableRepository.save(sampleTable);

        return toResponseV1(sampleTable);
    }

    @Cacheable(value = "SampleResponseV1", key = "#id")
    public SampleResponseV1 get(final int id) {
        return sampleTableRepository
                .findById(id)
                .map(SampleFactory::toResponseV1)
                .orElseThrow(() -> new ResourceNotFoundException("Sample Table", id));
    }

    @Caching(evict = {
            @CacheEvict(value = "SampleResponsePageV1", allEntries = true),
            @CacheEvict(value = "SampleResponseV1", key = "#id") })
    public void delete(final int id) {
        final var sampleTable = sampleTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sample Table", id));

        sampleTableRepository.delete(sampleTable);
    }
}
