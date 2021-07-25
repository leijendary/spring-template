package com.leijendary.spring.microservicetemplate.controller.v1;

import com.leijendary.spring.microservicetemplate.client.SampleClient;
import com.leijendary.spring.microservicetemplate.controller.AbstractController;
import com.leijendary.spring.microservicetemplate.data.request.QueryRequest;
import com.leijendary.spring.microservicetemplate.data.request.v1.SampleRequestV1;
import com.leijendary.spring.microservicetemplate.data.response.DataResponse;
import com.leijendary.spring.microservicetemplate.data.response.v1.SampleResponseV1;
import com.leijendary.spring.microservicetemplate.service.SampleTableService;
import com.leijendary.spring.microservicetemplate.util.RequestContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static com.leijendary.spring.microservicetemplate.controller.AbstractController.BASE_API_PATH;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

/**
 * This is an example of a controller that will be created in microservices.
 *
 * There are 3 parts of the {@link RequestMapping} url that we need to take note of:
 * 1. The api prefix ("api")
 * 2. The version ("v1")
 * 3. The parent path of this API ("/") which can be anything that this specific controller should be doing.
 *
 * Since this microservice uses a context path, the result of the url should be "/sample/api/v1"
 *
 * The url paths should be in kebab-case except for the query parameters, body, and other URL parts in which they
 * should be in camelCase.
 *
 * For headers, I would recommend that the Header keys should be in Pascal-Kebab-Case
 */
@RestController
@RequestMapping(BASE_API_PATH + "/v1")
@RequiredArgsConstructor
@Api("This is just a sample controller with a swagger documentation")
public class SampleControllerV1 extends AbstractController {

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
    public CompletableFuture<DataResponse<List<SampleResponseV1>>> list(
            final QueryRequest queryRequest, final Pageable pageable) {
        final var page = sampleTableService.list(queryRequest, pageable);
        final var response = DataResponse.<List<SampleResponseV1>>builder()
                .data(page.getContent())
                .meta(page)
                .links(page)
                .object(SampleResponseV1.class)
                .build();

        return completedFuture(response);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_urn:sample:create:v1')")
    @ResponseStatus(CREATED)
    @ApiOperation("Saves a sample record into the database")
    public CompletableFuture<DataResponse<SampleResponseV1>> create(
            @Valid @RequestBody final SampleRequestV1 request, final HttpServletResponse httpServletResponse) {
        final var sampleResponse = sampleTableService.create(request);
        final var response = DataResponse.<SampleResponseV1>builder()
                .data(sampleResponse)
                .status(CREATED)
                .object(SampleResponseV1.class)
                .build();

        locationHeader(httpServletResponse, sampleResponse.getId());

        return completedFuture(response);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('SCOPE_urn:sample:get:v1')")
    @ApiOperation("Retrieves the sample record from the database")
    public CompletableFuture<DataResponse<SampleResponseV1>> get(@PathVariable final long id) {
        final var sampleResponse = sampleTableService.get(id);
        final var response = DataResponse.<SampleResponseV1>builder()
                .data(sampleResponse)
                .object(SampleResponseV1.class)
                .build();

        return completedFuture(response);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('SCOPE_urn:sample:update:v1')")
    @ApiOperation("Updates the sample record into the database")
    public CompletableFuture<DataResponse<SampleResponseV1>> update(
            @PathVariable final long id, @Valid @RequestBody final SampleRequestV1 request) {
        final var sampleResponse = sampleTableService.update(id, request);
        final var response = DataResponse.<SampleResponseV1>builder()
                .data(sampleResponse)
                .object(SampleResponseV1.class)
                .build();

        return completedFuture(response);
    }

    @DeleteMapping ("{id}")
    @PreAuthorize("hasAuthority('SCOPE_urn:sample:delete:v1')")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Removes the sample record from the database")
    public CompletableFuture<Void> delete(@PathVariable final long id) {
        sampleTableService.delete(id);

        return completedFuture(null);
    }

    @GetMapping(value = "client", produces = TEXT_HTML_VALUE)
    public String client() {
        return sampleClient.homepage();
    }

    @GetMapping(value = "timezone", produces = TEXT_PLAIN_VALUE)
    public String timezone() {
        final var timeZone = RequestContextUtil.getTimeZone();
        final var zoneId = timeZone.toZoneId();
        final var displayName = zoneId.getDisplayName(TextStyle.FULL, Locale.getDefault());
        final var id = zoneId.getId();

        return String.format("%s %s", displayName, id);
    }

    @GetMapping(value = "locale", produces = TEXT_PLAIN_VALUE)
    public String locale() {
        return RequestContextUtil.getLocale().toString();
    }
}
