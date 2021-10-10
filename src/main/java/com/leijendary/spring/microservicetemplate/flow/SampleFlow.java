package com.leijendary.spring.microservicetemplate.flow;

import com.leijendary.spring.microservicetemplate.data.request.QueryRequest;
import com.leijendary.spring.microservicetemplate.data.request.v1.SampleRequestV1;
import com.leijendary.spring.microservicetemplate.data.response.v1.SampleResponseV1;
import com.leijendary.spring.microservicetemplate.mapper.SampleMapper;
import com.leijendary.spring.microservicetemplate.service.SampleSearchService;
import com.leijendary.spring.microservicetemplate.service.SampleTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SampleFlow {

    private static final SampleMapper MAPPER = SampleMapper.INSTANCE;

    private final SampleSearchService sampleSearchService;
    private final SampleTableService sampleTableService;

    public Page<SampleResponseV1> listV1(final QueryRequest queryRequest, final Pageable pageable) {
        return sampleTableService.list(queryRequest, pageable)
                .map(MAPPER::toResponseV1);
    }

    @Transactional
    public SampleResponseV1 createV1(final SampleRequestV1 request) {
        final var sampleData = MAPPER.toData(request);
        final var sampleTable = sampleTableService.create(sampleData);

        // Save the object to elasticsearch
        sampleSearchService.save(sampleTable);

        return MAPPER.toResponseV1(sampleTable);
    }

    public SampleResponseV1 getV1(final long id) {
        final var sampleTable = sampleTableService.get(id);

        return MAPPER.toResponseV1(sampleTable);
    }

    @Transactional
    public SampleResponseV1 updateV1(final long id, final SampleRequestV1 request) {
        final var sampleData = MAPPER.toData(request);
        final var sampleTable = sampleTableService.update(id, sampleData);

        // Update the object from elasticsearch
        sampleSearchService.update(sampleTable);

        return MAPPER.toResponseV1(sampleTable);
    }

    @Transactional
    public void deleteV1(final long id) {
        sampleTableService.delete(id);

        // Delete the object from elasticsearch
        sampleSearchService.delete(id);
    }
}
