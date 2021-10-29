package com.leijendary.spring.boot.template.flow;

import com.leijendary.spring.boot.core.data.request.QueryRequest;
import com.leijendary.spring.boot.core.flow.AppFlow;
import com.leijendary.spring.boot.template.data.response.v1.SampleSearchResponseV1;
import com.leijendary.spring.boot.template.mapper.SampleMapper;
import com.leijendary.spring.boot.template.service.SampleSearchService;
import com.leijendary.spring.boot.template.service.SampleTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SampleSearchFlow extends AppFlow {

    public static final SampleMapper MAPPER = SampleMapper.INSTANCE;

    private final SampleSearchService sampleSearchService;
    private final SampleTableService sampleTableService;

    public Page<SampleSearchResponseV1> listV1(final QueryRequest queryRequest, final Pageable pageable) {
        return sampleSearchService.list(queryRequest, pageable)
                .map(MAPPER::toSearchResponseV1);
    }

    public SampleSearchResponseV1 getV1(final UUID id) {
        final var serviceDocument = sampleSearchService.get(id);

        return MAPPER.toSearchResponseV1(serviceDocument);
    }

    public void reindexV1() {
        sampleTableService.all().forEach(sampleSearchService::save);
    }
}