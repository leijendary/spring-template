package com.leijendary.spring.boot.template.api.v1.service;

import java.util.UUID;

import com.leijendary.spring.boot.template.api.v1.data.SampleRequest;
import com.leijendary.spring.boot.template.api.v1.data.SampleResponse;
import com.leijendary.spring.boot.template.api.v1.mapper.SampleMapper;
import com.leijendary.spring.boot.template.api.v1.search.SampleSearch;
import com.leijendary.spring.boot.template.data.QueryRequest;
import com.leijendary.spring.boot.template.exception.ResourceNotFoundException;
import com.leijendary.spring.boot.template.repository.SampleTableRepository;
import com.leijendary.spring.boot.template.specification.SampleListSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SampleTableService {

    private static final String RESOURCE_NAME = "Sample Table";
    private static final SampleMapper MAPPER = SampleMapper.INSTANCE;

    private final SampleSearch sampleSearch;
    private final SampleTableRepository sampleTableRepository;

    public Page<SampleResponse> list(final QueryRequest queryRequest, final Pageable pageable) {
        final var specification = SampleListSpecification.builder()
                .query(queryRequest.getQuery())
                .build();

        return sampleTableRepository
                .findAll(specification, pageable)
                .map(MAPPER::toResponse);
    }

    @Transactional
    public SampleResponse create(final SampleRequest sampleRequest) {
        final var sampleTable = MAPPER.toEntity(sampleRequest);

        sampleTableRepository.save(sampleTable);

        // Save the object to elasticsearch
        sampleSearch.save(sampleTable);

        return MAPPER.toResponse(sampleTable);
    }

    public SampleResponse get(final UUID id) {
        return sampleTableRepository.findByIdAndDeletedAtIsNull(id)
                .map(MAPPER::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
    }

    @Transactional
    public SampleResponse update(final UUID id, final SampleRequest sampleRequest) {
        final var sampleTable = sampleTableRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));

        MAPPER.update(sampleRequest, sampleTable);

        sampleTableRepository.save(sampleTable);

        // Update the object from elasticsearch
        sampleSearch.update(sampleTable);

        return MAPPER.toResponse(sampleTable);
    }

    @Transactional
    public void delete(final UUID id) {
        final var sampleTable = sampleTableRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));

        sampleTableRepository.delete(sampleTable);

        // Delete the object from elasticsearch
        sampleSearch.delete(id);
    }

    @Transactional
    public void reindex() {
        sampleTableRepository.streamAll()
                .parallel()
                .forEach(sampleSearch::save);
    }
}
