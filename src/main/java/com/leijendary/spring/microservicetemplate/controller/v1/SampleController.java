package com.leijendary.spring.microservicetemplate.controller.v1;

import com.leijendary.spring.microservicetemplate.client.SampleClient;
import com.leijendary.spring.microservicetemplate.controller.AppController;
import com.leijendary.spring.microservicetemplate.data.AppPage;
import com.leijendary.spring.microservicetemplate.data.request.QueryRequest;
import com.leijendary.spring.microservicetemplate.data.request.SampleRequest;
import com.leijendary.spring.microservicetemplate.data.response.SampleResponse;
import com.leijendary.spring.microservicetemplate.service.SampleTableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * This is an example of a controller that will be created in microservices.
 *
 * There are 3 parts of the {@link RequestMapping} url that we need to take note of:
 * 1. The version ("v1")
 * 2. The parent path of this API ("/") which can be anything that this specific controller should be doing.
 *
 * This example has a server.servlet.context-path=/sample set so the path here should result into the following:
 * "/sample/v1/"
 *
 * The url paths should be in kebab-case except for the query parameters, body, and other URL parts in which they
 * should be in camelCase.
 *
 * For headers, I would recommend that the Header keys should be in Pascal-Kebab-Case
 *
 * Note: the {@link @RestController} annotation has a value of "<ControllerName><version>" because on multiple
 * versioned rest controllers, Mocky will throw an error when the same controller name is found even though
 * they have different mappings. The rest controller value will fix that
 */
@RestController("SampleControllerV1")
@RequestMapping("v1")
@RequiredArgsConstructor
@Api("This is just a sample controller with a swagger documentation")
public class SampleController extends AppController {

    private final SampleClient sampleClient;
    private final SampleTableService sampleTableService;

    /**
     * This is a sample RequestMapping (Only GET method, that is why i used {@link GetMapping})
     *
     * @param pageable The page request. Since this API has pageable results, it is recommended that the request
     *                 parameters contains {@link Pageable}
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_urn:sample:list:v1')")
    @ApiOperation("Sample implementation of swagger in a api")
    public CompletableFuture<AppPage<SampleResponse>> list(QueryRequest queryRequest, Pageable pageable) {
        final var page = sampleTableService.list(queryRequest, pageable);

        return completedFuture(page);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_urn:sample:create:v1')")
    @ResponseStatus(CREATED)
    @ApiOperation("Saves a sample record into the database")
    public CompletableFuture<SampleResponse> create(@RequestBody SampleRequest request, HttpServletResponse response) {
        final var sampleResponse = sampleTableService.create(request);

        locationHeader(response, sampleResponse.getId());

        return completedFuture(sampleResponse);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('SCOPE_urn:sample:get:v1')")
    @ApiOperation("Retrieves the sample record from the database")
    public CompletableFuture<SampleResponse> get(@PathVariable int id) {
        final var sampleResponse = sampleTableService.get(id);

        return completedFuture(sampleResponse);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('SCOPE_urn:sample:update:v1')")
    @ApiOperation("Updates the sample record into the database")
    public CompletableFuture<SampleResponse> update(@PathVariable int id, @RequestBody SampleRequest request) {
        final var sampleResponse = sampleTableService.update(id, request);

        return completedFuture(sampleResponse);
    }

    @DeleteMapping ("{id}")
    @PreAuthorize("hasAuthority('SCOPE_urn:sample:delete:v1')")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Removes the sample record from the database")
    public CompletableFuture<Void> delete(@PathVariable int id) {
        sampleTableService.delete(id);

        return completedFuture(null);
    }

    @GetMapping(value = "client", produces = TEXT_HTML_VALUE)
    public String client() {
        return sampleClient.homepage();
    }
}
