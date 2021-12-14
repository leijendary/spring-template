package com.leijendary.spring.boot.template.api.v1.controller;

import java.util.List;
import java.util.UUID;

import com.leijendary.spring.boot.template.api.v1.data.SampleSearchResponse;
import com.leijendary.spring.boot.template.api.v1.search.SampleSearch;
import com.leijendary.spring.boot.template.api.v1.service.SampleTableService;
import com.leijendary.spring.boot.template.data.DataResponse;
import com.leijendary.spring.boot.template.data.QueryRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/samples/search")
@RequiredArgsConstructor
@Api("Sample search resource API")
public class SampleSearchController {

    private final SampleSearch sampleSearch;
    private final SampleTableService sampleTableService;

    @GetMapping
    @ApiOperation("List all the objects based on the query parameter")
    public DataResponse<List<SampleSearchResponse>> list(final QueryRequest queryRequest, final Pageable pageable) {
        final var page = sampleSearch.list(queryRequest, pageable);

        return DataResponse.<List<SampleSearchResponse>>builder()
                .data(page.getContent())
                .meta(page)
                .links(page)
                .object(SampleSearchResponse.class)
                .build();
    }

    @GetMapping("{id}")
    @ApiOperation("Get the specific object using the ID in elasticsearch")
    public DataResponse<SampleSearchResponse> get(@PathVariable final UUID id) {
        final var sampleResponse = sampleSearch.get(id);

        return DataResponse.<SampleSearchResponse>builder()
                .data(sampleResponse)
                .object(SampleSearchResponse.class)
                .build();
    }

    @PostMapping("reindex")
    @ApiOperation("Reindex all objects")
    public void reindex() {
        sampleTableService.reindex();
    }
}
