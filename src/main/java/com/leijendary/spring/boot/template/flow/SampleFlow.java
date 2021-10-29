package com.leijendary.spring.boot.template.flow;

import com.leijendary.spring.boot.core.data.request.QueryRequest;
import com.leijendary.spring.boot.core.flow.AppFlow;
import com.leijendary.spring.boot.template.data.request.v1.SampleRequestV1;
import com.leijendary.spring.boot.template.data.response.v1.SampleResponseV1;
import com.leijendary.spring.boot.template.mapper.SampleMapper;
import com.leijendary.spring.boot.template.service.SampleSearchService;
import com.leijendary.spring.boot.template.service.SampleTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SampleFlow extends AppFlow {

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

    public SampleResponseV1 getV1(final UUID id) {
        final var sampleTable = sampleTableService.get(id);

        return MAPPER.toResponseV1(sampleTable);
    }

    @Transactional
    public SampleResponseV1 updateV1(final UUID id, final SampleRequestV1 request) {
        final var sampleData = MAPPER.toData(request);
        final var sampleTable = sampleTableService.update(id, sampleData);

        // Update the object from elasticsearch
        sampleSearchService.update(sampleTable);

        return MAPPER.toResponseV1(sampleTable);
    }

    @Transactional
    public void deleteV1(final UUID id) {
        sampleTableService.delete(id);

        // Delete the object from elasticsearch
        sampleSearchService.delete(id);
    }
}