package com.leijendary.spring.boot.template.service;

import com.leijendary.spring.boot.core.data.request.QueryRequest;
import com.leijendary.spring.boot.core.exception.ResourceNotFoundException;
import com.leijendary.spring.boot.core.service.AppService;
import com.leijendary.spring.boot.template.document.SampleDocument;
import com.leijendary.spring.boot.template.mapper.SampleMapper;
import com.leijendary.spring.boot.template.model.SampleTable;
import com.leijendary.spring.boot.template.repository.SampleSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.leijendary.spring.boot.core.util.SearchUtil.match;
import static com.leijendary.spring.boot.core.util.SearchUtil.sortBuilders;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class SampleSearchService extends AppService {

    private static final String RESOURCE_NAME = "Sample Document";
    private static final SampleMapper MAPPER = SampleMapper.INSTANCE;

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    private final SampleSearchRepository serviceSearchRepository;

    public Page<SampleDocument> list(final QueryRequest queryRequest, final Pageable pageable) {
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

        return new PageImpl<>(list, pageable, total);
    }

    public SampleDocument save(final SampleTable sampleTable) {
        final var document = MAPPER.toDocument(sampleTable);

        return serviceSearchRepository.save(document);
    }

    public SampleDocument get(final UUID id) {
        return serviceSearchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
    }

    public SampleDocument update(final SampleTable sampleTable) {
        final var id = sampleTable.getId();
        final var document = get(id);

        MAPPER.update(sampleTable, document);

        return serviceSearchRepository.save(document);
    }

    public void delete(final UUID id) {
        final var document = get(id);

        serviceSearchRepository.delete(document);
    }
}
