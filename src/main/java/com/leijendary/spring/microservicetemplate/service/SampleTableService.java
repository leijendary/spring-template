package com.leijendary.spring.microservicetemplate.service;

import com.leijendary.spring.microservicetemplate.data.SampleData;
import com.leijendary.spring.microservicetemplate.data.request.QueryRequest;
import com.leijendary.spring.microservicetemplate.exception.ResourceNotFoundException;
import com.leijendary.spring.microservicetemplate.exception.ResourceNotUniqueException;
import com.leijendary.spring.microservicetemplate.factory.SampleFactory;
import com.leijendary.spring.microservicetemplate.model.SampleTable;
import com.leijendary.spring.microservicetemplate.repository.SampleTableRepository;
import com.leijendary.spring.microservicetemplate.specification.SampleListSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SampleTableService extends AbstractService {

    private static final String CACHE_PAGE = "SampleTablePage";
    private static final String CACHE = "SampleTable";
    private static final String RESOURCE_NAME = "Sample Table";

    private final SampleTableRepository sampleTableRepository;

    @Cacheable(value = CACHE_PAGE, key = "#queryRequest.toString() + '|' + #pageable.toString()")
    public Page<SampleTable> list(final QueryRequest queryRequest, final Pageable pageable) {
        final var specification = SampleListSpecification.builder()
                .query(queryRequest.getQuery())
                .build();

        return sampleTableRepository.findAll(specification, pageable);
    }

    @Caching(
            evict = @CacheEvict(value = CACHE_PAGE, allEntries = true),
            put = @CachePut(value = CACHE, key = "#result.id"))
    @Transactional
    public SampleTable create(final SampleData sampleData) {
        final var sampleTable = SampleFactory.of(sampleData);
        final var column1 = sampleData.getColumn1();

        // Validate the column1 field
        validateColumn1(column1, 0);

        // Set the reference of each translation first
        sampleTable.getTranslations().forEach(translation -> translation.setReference(sampleTable));

        return sampleTableRepository.save(sampleTable);
    }

    @Cacheable(value = CACHE, key = "#id")
    public SampleTable get(final long id) {
        return sampleTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
    }

    @Caching(evict = {
            @CacheEvict(value = CACHE_PAGE, allEntries = true),
            @CacheEvict(value = CACHE, key = "#id") })
    @Transactional
    public SampleTable update(final long id, final SampleData sampleData) {
        final var sampleTable = get(id);
        final var column1 = sampleData.getColumn1();

        // Validate the column1 field
        validateColumn1(column1, id);

        SampleFactory.map(sampleData, sampleTable);

        // Set the reference of each translation first
        sampleTable.getTranslations().forEach(translation -> translation.setReference(sampleTable));

        return sampleTableRepository.save(sampleTable);
    }

    @Caching(evict = {
            @CacheEvict(value = CACHE_PAGE, allEntries = true),
            @CacheEvict(value = CACHE, key = "#id") })
    @Transactional
    public void delete(final long id) {
        final var sampleTable = get(id);

        sampleTableRepository.delete(sampleTable);
    }

    public List<SampleTable> all() {
        return sampleTableRepository.findAll();
    }

    private void validateColumn1(final String column1, final long id) {
        final var exists = sampleTableRepository.existsByColumn1IgnoreCaseAndIdNot(column1, id);

        if (exists) {
            throw new ResourceNotUniqueException("column1", column1);
        }
    }
}
