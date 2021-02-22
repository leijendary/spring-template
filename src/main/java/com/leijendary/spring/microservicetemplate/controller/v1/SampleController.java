package com.leijendary.spring.microservicetemplate.controller.v1;

import com.leijendary.spring.microservicetemplate.client.SampleClient;
import com.leijendary.spring.microservicetemplate.controller.AppController;
import com.leijendary.spring.microservicetemplate.data.AppPage;
import com.leijendary.spring.microservicetemplate.data.request.QueryRequest;
import com.leijendary.spring.microservicetemplate.data.request.SampleRequest;
import com.leijendary.spring.microservicetemplate.data.response.SampleResponse;
import com.leijendary.spring.microservicetemplate.service.SampleTableService;
import com.leijendary.spring.microservicetemplate.validator.SampleRequestValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.created;

/**
 * This is an example of a controller that will be created in microservices.
 *
 * There are 3 parts of the {@link RequestMapping} url that we need to take note of:
 * 1. The prefix ("/api")
 * 2. The version ("/v1")
 * 3. The parent path of this API ("/sample") which can be anything that this specific controller should be doing.
 *
 * The url paths should be in kebab-case except for the query parameters, body, and other URL parts in which they
 * should be in camelCase.
 *
 * For headers, I would recommend that the Header keys should be in Pascal-Kebab-Case
 */
@RestController
@RequestMapping("/api/v1/sample")
@RequiredArgsConstructor
@Api("This is just a sample controller with a swagger documentation")
public class SampleController extends AppController {

    private final SampleClient sampleClient;
    private final SampleTableService sampleTableService;
    private final SampleRequestValidator sampleRequestValidator;

    /**
     * This is a sample RequestMapping (Only GET method, that is why i used {@link GetMapping})
     *
     * @param pageable The page request. Since this API has pageable results, it is recommended that the request
     *                 parameters contains {@link Pageable}
     */
    @GetMapping
    // @PreAuthorize("hasAuthority('sample:list')")
    @ApiOperation("Sample implementation of swagger in a api")
    public CompletableFuture<AppPage<SampleResponse>> list(QueryRequest queryRequest, Pageable pageable) {
        final var page = sampleTableService.list(queryRequest, pageable);

        return completedFuture(page);
    }

    @PostMapping
    // @PreAuthorize("hasAuthority('sample:create')")
    @ResponseStatus(CREATED)
    @ApiOperation("Saves a sample record into the database")
    public CompletableFuture<ResponseEntity<SampleResponse>> create(@RequestBody SampleRequest request) {
        validate(sampleRequestValidator, request, SampleRequest.class);

        final var sampleResponse = sampleTableService.create(request);
        final var url = URI.create("/api/v1/sample/" + sampleResponse.getId());
        final var response = created(url).body(sampleResponse);

        return completedFuture(response);
    }

    @GetMapping("{id}")
    // @PreAuthorize("hasAuthority('sample:get')")
    @ApiOperation("Retrieves the sample record from the database")
    public CompletableFuture<SampleResponse> get(@PathVariable int id) {
        final var sampleResponse = sampleTableService.get(id);

        return completedFuture(sampleResponse);
    }

    @PutMapping("{id}")
    // @PreAuthorize("hasAuthority('sample:update')")
    @ApiOperation("Updates the sample record into the database")
    public CompletableFuture<SampleResponse> update(@PathVariable int id, @RequestBody SampleRequest request) {
        validate(sampleRequestValidator, request, SampleRequest.class);

        final var sampleResponse = sampleTableService.update(id, request);

        return completedFuture(sampleResponse);
    }

    @DeleteMapping ("{id}")
    // @PreAuthorize("hasAuthority('sample:delete')")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Removes the sample record from the database")
    public CompletableFuture<Void> delete(@PathVariable int id, @RequestBody SampleRequest request) {
        validate(sampleRequestValidator, request, SampleRequest.class);

        sampleTableService.delete(id);

        return completedFuture(null);
    }

    @GetMapping(value = "client", produces = MediaType.TEXT_HTML_VALUE)
    public String client() {
        return sampleClient.homepage();
    }
}
