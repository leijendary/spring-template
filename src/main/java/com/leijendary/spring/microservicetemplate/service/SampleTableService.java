package com.leijendary.spring.microservicetemplate.service;

import com.leijendary.spring.microservicetemplate.data.SampleData;
import com.leijendary.spring.microservicetemplate.data.request.QueryRequest;
import com.leijendary.spring.microservicetemplate.exception.ResourceNotFoundException;
import com.leijendary.spring.microservicetemplate.exception.ResourceNotUniqueException;
import com.leijendary.spring.microservicetemplate.mapper.SampleMapper;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SampleTableService extends AbstractService {

    private static final String CACHE_PAGE = "SampleTablePage";
    private static final String CACHE = "SampleTable";
    private static final String RESOURCE_NAME = "Sample Table";
    private static final SampleMapper MAPPER = SampleMapper.INSTANCE;

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
        final var sampleTable = MAPPER.toEntity(sampleData);
        final var column1 = sampleData.getColumn1();

        // Validate the column1 field
        validateColumn1(column1, null);

        return sampleTableRepository.save(sampleTable);
    }

    @Cacheable(value = CACHE, key = "#id")
    public SampleTable get(final UUID id) {
        return sampleTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
    }

    @Caching(
            evict = @CacheEvict(value = CACHE_PAGE, allEntries = true),
            put = @CachePut(value = CACHE, key = "#id"))
    @Transactional
    public SampleTable update(final UUID id, final SampleData sampleData) {
        final var sampleTable = get(id);
        final var column1 = sampleData.getColumn1();

        // Validate the column1 field
        validateColumn1(column1, id);

        MAPPER.update(sampleData, sampleTable);

        return sampleTableRepository.save(sampleTable);
    }

    @Caching(evict = {
            @CacheEvict(value = CACHE_PAGE, allEntries = true),
            @CacheEvict(value = CACHE, key = "#id")
    })
    @Transactional
    public void delete(final UUID id) {
        final var sampleTable = get(id);

        sampleTableRepository.delete(sampleTable);
    }

    public List<SampleTable> all() {
        return sampleTableRepository.findAll();
    }

    private void validateColumn1(final String column1, final UUID id) {
        boolean exists;

        if (id == null) {
            exists = sampleTableRepository.existsByColumn1IgnoreCase(column1);
        } else {
            exists = sampleTableRepository.existsByColumn1IgnoreCaseAndIdNot(column1, id);
        }

        if (exists) {
            throw new ResourceNotUniqueException("column1", column1);
        }
    }
}
