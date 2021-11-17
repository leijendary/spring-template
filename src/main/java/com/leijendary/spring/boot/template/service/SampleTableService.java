package com.leijendary.spring.boot.template.service;

import java.util.UUID;
import java.util.stream.Stream;

import com.leijendary.spring.boot.core.data.request.QueryRequest;
import com.leijendary.spring.boot.core.exception.ResourceNotFoundException;
import com.leijendary.spring.boot.core.service.AppService;
import com.leijendary.spring.boot.template.data.SampleData;
import com.leijendary.spring.boot.template.mapper.SampleMapper;
import com.leijendary.spring.boot.template.model.SampleTable;
import com.leijendary.spring.boot.template.repository.SampleTableRepository;
import com.leijendary.spring.boot.template.specification.SampleListSpecification;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SampleTableService extends AppService {

    private static final String CACHE = "SampleTable";
    private static final String RESOURCE_NAME = "Sample Table";
    private static final SampleMapper MAPPER = SampleMapper.INSTANCE;

    private final SampleTableRepository sampleTableRepository;

    public Page<SampleTable> list(final QueryRequest queryRequest, final Pageable pageable) {
        final var specification = SampleListSpecification.builder()
                .query(queryRequest.getQuery())
                .build();

        return sampleTableRepository.findAll(specification, pageable);
    }

    @CachePut(value = CACHE, key = "#result.id")
    @Transactional
    public SampleTable create(final SampleData sampleData) {
        final var sampleTable = MAPPER.toEntity(sampleData);

        return sampleTableRepository.save(sampleTable);
    }

    @Cacheable(value = CACHE, key = "#id")
    public SampleTable get(final UUID id) {
        return sampleTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
    }

    @CachePut(value = CACHE, key = "#result.id")
    @Transactional
    public SampleTable update(final UUID id, final SampleData sampleData) {
        final var sampleTable = get(id);

        MAPPER.update(sampleData, sampleTable);

        return sampleTableRepository.save(sampleTable);
    }

    @CacheEvict(value = CACHE, key = "#id")
    @Transactional
    public void delete(final UUID id) {
        final var sampleTable = get(id);

        sampleTableRepository.delete(sampleTable);
    }

    @Transactional
    public Stream<SampleTable> streamAll() {
        return sampleTableRepository.streamAll();
    }
}
