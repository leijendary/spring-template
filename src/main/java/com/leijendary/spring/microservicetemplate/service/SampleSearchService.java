package com.leijendary.spring.microservicetemplate.service;

import com.leijendary.spring.microservicetemplate.data.request.QueryRequest;
import com.leijendary.spring.microservicetemplate.document.SampleDocument;
import com.leijendary.spring.microservicetemplate.exception.ResourceNotFoundException;
import com.leijendary.spring.microservicetemplate.mapper.SampleMapper;
import com.leijendary.spring.microservicetemplate.model.SampleTable;
import com.leijendary.spring.microservicetemplate.repository.SampleSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import static com.leijendary.spring.microservicetemplate.util.SearchUtil.sortBuilders;
import static com.leijendary.spring.microservicetemplate.util.SearchUtil.wildcard;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class SampleSearchService extends AbstractService {

    private static final String RESOURCE_NAME = "Sample Document";
    private static final SampleMapper MAPPER = SampleMapper.INSTANCE;

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    private final SampleSearchRepository serviceSearchRepository;

    public Page<SampleDocument> list(final QueryRequest queryRequest, final Pageable pageable) {
        final var query = queryRequest.getQuery();
        final var searchBuilder = new NativeSearchQueryBuilder();
        // Query for translations.name and translations.description
        final var wildcard = wildcard(query, "translations.name", "translations.description");

        // Add the query for the actual search
        searchBuilder.withQuery(wildcard);
        // Add the pagination to the search builder
        searchBuilder.withPageable(pageable);

        // Each sort builder should be added into the search builder's sort
        final var sortBuilders = sortBuilders(pageable);
        sortBuilders.forEach(searchBuilder::withSort);

        final var searchQuery = searchBuilder.build();
        final var searchHits = elasticsearchRestTemplate.search(searchQuery,
                SampleDocument.class);
        final var list = searchHits
                .stream()
                .map(SearchHit::getContent)
                .collect(toList());
        final var total = searchHits.getTotalHits();

        return new PageImpl<>(list, pageable, total);
    }

    public SampleDocument save(final SampleTable sampleTable) {
        final var serviceDocument = MAPPER.toDocument(sampleTable);

        return serviceSearchRepository.save(serviceDocument);
    }

    public SampleDocument get(final long id) {
        return serviceSearchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
    }

    public SampleDocument update(final SampleTable sampleTable) {
        final var id = sampleTable.getId();
        final var sampleDocument = get(id);

        MAPPER.update(sampleTable, sampleDocument);

        return serviceSearchRepository.save(sampleDocument);
    }

    public void delete(final long id) {
        final var serviceDocument = get(id);

        serviceSearchRepository.delete(serviceDocument);
    }
}
