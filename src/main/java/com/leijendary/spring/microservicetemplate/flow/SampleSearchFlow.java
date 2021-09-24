package com.leijendary.spring.microservicetemplate.flow;

import com.leijendary.spring.microservicetemplate.data.request.QueryRequest;
import com.leijendary.spring.microservicetemplate.data.response.v1.SampleSearchResponseV1;
import com.leijendary.spring.microservicetemplate.factory.SampleDocumentFactory;
import com.leijendary.spring.microservicetemplate.service.SampleSearchService;
import com.leijendary.spring.microservicetemplate.service.SampleTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import static com.leijendary.spring.microservicetemplate.factory.SampleDocumentFactory.toResponseV1;

@Component
@RequiredArgsConstructor
public class SampleSearchFlow {

    private final SampleSearchService sampleSearchService;
    private final SampleTableService sampleTableService;

    public Page<SampleSearchResponseV1> listV1(final QueryRequest queryRequest, final Pageable pageable) {
        return sampleSearchService.list(queryRequest, pageable)
                .map(SampleDocumentFactory::toResponseV1);
    }

    public SampleSearchResponseV1 getV1(final long id) {
        final var serviceDocument = sampleSearchService.get(id);

        return toResponseV1(serviceDocument);
    }

    public void reindexV1() {
        sampleTableService.all().forEach(sampleSearchService::save);
    }
}
