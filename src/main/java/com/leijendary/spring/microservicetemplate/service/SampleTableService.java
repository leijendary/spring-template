package com.leijendary.spring.microservicetemplate.service;

import com.leijendary.spring.microservicetemplate.data.AppPage;
import com.leijendary.spring.microservicetemplate.data.request.QueryRequest;
import com.leijendary.spring.microservicetemplate.data.request.SampleRequest;
import com.leijendary.spring.microservicetemplate.data.response.SampleResponse;
import com.leijendary.spring.microservicetemplate.event.producer.SampleResponseProducer;
import com.leijendary.spring.microservicetemplate.exception.ResourceNotFoundException;
import com.leijendary.spring.microservicetemplate.factory.AppPageFactory;
import com.leijendary.spring.microservicetemplate.factory.SampleFactory;
import com.leijendary.spring.microservicetemplate.repository.SampleTableRepository;
import com.leijendary.spring.microservicetemplate.specification.SampleListSpecification;
import com.leijendary.spring.microservicetemplate.validator.SampleRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleTableService extends AppService {

    private final SampleResponseProducer sampleResponseProducer;
    private final SampleRequestValidator sampleRequestValidator;
    private final SampleTableRepository sampleTableRepository;

    @Cacheable(value = "SampleResponsePage", key = "#queryRequest.toString() + '|' + #pageable.toString()")
    public AppPage<SampleResponse> list(final QueryRequest queryRequest, final Pageable pageable) {
        final var specification = SampleListSpecification.builder()
                .column1(queryRequest.getQuery())
                .build();
        final var page = sampleTableRepository.findAll(specification, pageable)
                .map(SampleFactory::toResponse);

        return AppPageFactory.of(page);
    }

    @CachePut(value = "SampleResponse", key = "#result.id")
    public SampleResponse create(final SampleRequest sampleRequest) {
        validate(sampleRequestValidator, sampleRequest, SampleRequest.class);

        final var sampleTable = SampleFactory.of(sampleRequest);

        sampleTableRepository.save(sampleTable);

        final var sampleResponse = SampleFactory.toResponse(sampleTable);

        sampleResponseProducer.created1(sampleResponse);

        return sampleResponse;
    }

    @CachePut(value = "SampleResponse", key = "#id")
    public SampleResponse update(final int id, final SampleRequest sampleRequest) {
        final var sampleTable = sampleTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sample Table", id));

        validate(sampleRequestValidator, sampleRequest, SampleRequest.class);

        SampleFactory.map(sampleRequest, sampleTable);

        sampleTableRepository.save(sampleTable);

        final var sampleResponse = SampleFactory.toResponse(sampleTable);

        sampleResponseProducer.updated1(sampleResponse);

        return sampleResponse;
    }

    @Cacheable(value = "SampleResponse", key = "#id")
    public SampleResponse get(final int id) {
        return sampleTableRepository
                .findById(id)
                .map(SampleFactory::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Sample Table", id));
    }

    @Caching(evict = {
            @CacheEvict(value = "SampleResponsePage", allEntries = true),
            @CacheEvict(value = "SampleResponse", key = "#id") })
    public void delete(final int id) {
        final var sampleTable = sampleTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sample Table", id));
        final var sampleResponse = SampleFactory.toResponse(sampleTable);

        sampleTableRepository.delete(sampleTable);

        sampleResponseProducer.deleted1(sampleResponse);
    }
}
