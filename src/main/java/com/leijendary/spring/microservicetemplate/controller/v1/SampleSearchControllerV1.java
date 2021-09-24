package com.leijendary.spring.microservicetemplate.controller.v1;

import com.leijendary.spring.microservicetemplate.controller.AbstractController;
import com.leijendary.spring.microservicetemplate.data.request.QueryRequest;
import com.leijendary.spring.microservicetemplate.data.response.DataResponse;
import com.leijendary.spring.microservicetemplate.data.response.v1.SampleSearchResponseV1;
import com.leijendary.spring.microservicetemplate.flow.SampleSearchFlow;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.leijendary.spring.microservicetemplate.controller.AbstractController.BASE_API_PATH;
import static java.util.concurrent.CompletableFuture.completedFuture;

@RestController
@RequestMapping(BASE_API_PATH + "/v1/samples/search")
@RequiredArgsConstructor
@Api("Sample search resource API")
public class SampleSearchControllerV1 extends AbstractController {

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
    public CompletableFuture<DataResponse<SampleSearchResponseV1>> get(@PathVariable final long id) {
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
