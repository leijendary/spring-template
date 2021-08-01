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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleTableService extends AbstractService {

    private static final String RESOURCE_NAME = "Sample Table";

    private final SampleTableRepository sampleTableRepository;

    public Page<SampleTable> list(final QueryRequest queryRequest, final Pageable pageable) {
        final var specification = SampleListSpecification.builder()
                .query(queryRequest.getQuery())
                .build();

        return sampleTableRepository.findAll(specification, pageable);
    }

    public SampleTable create(final SampleData sampleData) {
        final var sampleTable = SampleFactory.of(sampleData);

        sampleTableRepository
                .findFirstByColumn1IgnoreCaseAndIdNot(sampleData.getColumn1(), 0)
                .ifPresent(sampleTable1 -> {
                    throw new ResourceNotUniqueException("column1", sampleData.getColumn1());
                });

        sampleTableRepository.save(sampleTable);

        return sampleTable;
    }

    public SampleTable get(final long id) {
        return sampleTableRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
    }

    public SampleTable update(final long id, final SampleData sampleData) {
        var sampleTable = sampleTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));

        sampleTableRepository
                .findFirstByColumn1IgnoreCaseAndIdNot(sampleData.getColumn1(), id)
                .ifPresent(sampleTable1 -> {
                    throw new ResourceNotUniqueException("column1", sampleData.getColumn1());
                });

        SampleFactory.map(sampleData, sampleTable);

        return sampleTableRepository.save(sampleTable);
    }

    public void delete(final long id) {
        final var sampleTable = sampleTableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));

        sampleTableRepository.delete(sampleTable);
    }
}
