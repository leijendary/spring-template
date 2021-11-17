package com.leijendary.spring.boot.template.controller.v1;

import static com.leijendary.spring.boot.template.controller.AppController.BASE_API_PATH;
import static java.util.concurrent.CompletableFuture.completedFuture;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.leijendary.spring.boot.template.controller.AppController;
import com.leijendary.spring.boot.template.data.request.QueryRequest;
import com.leijendary.spring.boot.template.data.response.DataResponse;
import com.leijendary.spring.boot.template.data.response.v1.SampleSearchResponseV1;
import com.leijendary.spring.boot.template.flow.SampleSearchFlow;

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
@RequestMapping(BASE_API_PATH + "/v1/samples/search")
@RequiredArgsConstructor
@Api("Sample search resource API")
public class SampleSearchControllerV1 extends AppController {

    private final SampleSearchFlow sampleSearchFlow;

    @GetMapping
    @ApiOperation("List all the objects based on the query parameter")
    public CompletableFuture<DataResponse<List<SampleSearchResponseV1>>> list(
            final QueryRequest queryRequest, final Pageable pageable) {
        final var page = sampleSearchFlow.listV1(queryRequest, pageable);
        final var response = DataResponse.<List<SampleSearchResponseV1>>builder()
                .data(page.getContent())
                .meta(page)
                .links(page)
                .object(SampleSearchResponseV1.class)
                .build();

        return completedFuture(response);
    }

    @GetMapping("{id}")
    @ApiOperation("Get the specific object using the ID in elasticsearch")
    public CompletableFuture<DataResponse<SampleSearchResponseV1>> get(@PathVariable final UUID id) {
        final var sampleResponse = sampleSearchFlow.getV1(id);
        final var response = DataResponse.<SampleSearchResponseV1>builder()
                .data(sampleResponse)
                .object(SampleSearchResponseV1.class)
                .build();

        return completedFuture(response);
    }

    @PostMapping("reindex")
    @ApiOperation("Reindex all objects")
    public CompletableFuture<Void> reindex() {
        sampleSearchFlow.reindexV1();

        return completedFuture(null);
    }
}
