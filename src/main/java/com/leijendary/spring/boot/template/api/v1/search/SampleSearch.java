package com.leijendary.spring.boot.template.api.v1.search;

import static com.leijendary.spring.boot.template.util.SearchUtil.match;
import static com.leijendary.spring.boot.template.util.SearchUtil.sortBuilders;
import static java.util.stream.Collectors.toList;

import java.util.UUID;

import com.leijendary.spring.boot.template.api.v1.data.SampleSearchResponse;
import com.leijendary.spring.boot.template.api.v1.mapper.SampleMapper;
import com.leijendary.spring.boot.template.data.QueryRequest;
import com.leijendary.spring.boot.template.document.SampleDocument;
import com.leijendary.spring.boot.template.exception.ResourceNotFoundException;
import com.leijendary.spring.boot.template.model.SampleTable;
import com.leijendary.spring.boot.template.repository.SampleSearchRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SampleSearch {

    private static final String RESOURCE_NAME = "Sample Document";
    private static final SampleMapper MAPPER = SampleMapper.INSTANCE;

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    private final SampleSearchRepository serviceSearchRepository;

    public Page<SampleSearchResponse> list(final QueryRequest queryRequest, final Pageable pageable) {
        final var query = queryRequest.getQuery();
        final var searchBuilder = new NativeSearchQueryBuilder();
        // Query for translations.name and translations.description
        final var boolQuery = match(query, "translations.name", "translations.description");

        // Add the query for the actual search
        searchBuilder.withQuery(boolQuery);
        // Add the pagination to the search builder
        searchBuilder.withPageable(pageable);

        // Each sort builder should be added into the search builder's sort
        final var sortBuilders = sortBuilders(pageable);
        sortBuilders.forEach(searchBuilder::withSort);

        final var searchQuery = searchBuilder.build();
        final var searchHits = elasticsearchRestTemplate.search(
                searchQuery, SampleDocument.class);
        final var list = searchHits
                .stream()
                .map(SearchHit::getContent)
                .collect(toList());
        final var total = searchHits.getTotalHits();

        return new PageImpl<>(list, pageable, total)
                .map(MAPPER::toSearchResponse);
    }

    public void save(final SampleTable sampleTable) {
        final var document = MAPPER.toDocument(sampleTable);

        serviceSearchRepository.save(document);
    }

    public SampleSearchResponse get(final UUID id) {
        return serviceSearchRepository.findById(id)
                .map(MAPPER::toSearchResponse)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
    }

    public void update(final SampleTable sampleTable) {
        final var id = sampleTable.getId();
        final var document = serviceSearchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));

        MAPPER.update(sampleTable, document);

        serviceSearchRepository.save(document);
    }

    public void delete(final UUID id) {
        final var document = serviceSearchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));

        serviceSearchRepository.delete(document);
    }
}
