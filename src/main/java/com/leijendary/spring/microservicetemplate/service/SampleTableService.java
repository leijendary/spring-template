package com.leijendary.spring.microservicetemplate.service;

import com.leijendary.spring.microservicetemplate.cache.SampleResponseCache;
import com.leijendary.spring.microservicetemplate.cache.SampleResponsePageCache;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class SampleTableService extends AppService {

    private final SampleResponseCache sampleResponseCache;
    private final SampleResponsePageCache sampleResponsePageCache;
    private final SampleResponseProducer sampleResponseProducer;
    private final SampleRequestValidator sampleRequestValidator;
    private final SampleTableRepository sampleTableRepository;

    public AppPage<SampleResponse> list(final QueryRequest queryRequest, final Pageable pageable) {
        final var cacheKey = pageCacheKey(queryRequest, pageable);
        Supplier<AppPage<SampleResponse>> supplier = () -> {
            final var specification = SampleListSpecification.builder()
                    .column1(queryRequest.getQuery())
                    .build();

            return AppPageFactory.of(sampleTableRepository
                    .findAll(specification, pageable)
                    .map(SampleFactory::toResponse));
        };

        return ofNullable(sampleResponsePageCache.get(cacheKey))
                .orElseGet(supplier);
    }

    public SampleResponse create(final SampleRequest sampleRequest) {
        validate(sampleRequestValidator, sampleRequest, SampleRequest.class);

        final var sampleTable = SampleFactory.of(sampleRequest);

        sampleTableRepository.save(sampleTable);

        final var sampleResponse = SampleFactory.toResponse(sampleTable);

        sampleResponseCache.put(sampleResponse.getId(), sampleResponse);

        sampleResponseProducer.created1(sampleResponse);

        return sampleResponse;
    }

    public SampleResponse update(final int id, final SampleRequest sampleRequest) {
        final var sampleTable = sampleTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sample Table", id));

        validate(sampleRequestValidator, sampleRequest, SampleRequest.class);

        SampleFactory.map(sampleRequest, sampleTable);

        sampleTableRepository.save(sampleTable);

        final var sampleResponse = SampleFactory.toResponse(sampleTable);

        sampleResponseCache.put(sampleResponse.getId(), sampleResponse);

        sampleResponseProducer.updated1(sampleResponse);

        return sampleResponse;
    }

    public SampleResponse get(final int id) {
        Supplier<SampleResponse> supplier = () -> {
            final var sampleResponse = sampleTableRepository
                    .findById(id)
                    .map(SampleFactory::toResponse)
                    .orElseThrow(() -> new ResourceNotFoundException("Sample Table", id));

            sampleResponseCache.put(id, sampleResponse);

            return sampleResponse;
        };

        return ofNullable(sampleResponseCache.get(id))
                .orElseGet(supplier);
    }

    public void delete(final int id) {
        final var sampleTable = sampleTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sample Table", id));
        final var sampleResponse = SampleFactory.toResponse(sampleTable);

        sampleTableRepository.delete(sampleTable);

        sampleResponseProducer.deleted1(sampleResponse);
    }

    private String pageCacheKey(final QueryRequest queryRequest, final Pageable pageable) {
        return queryRequest.toString() + "|" + pageable.toString();
    }
}
