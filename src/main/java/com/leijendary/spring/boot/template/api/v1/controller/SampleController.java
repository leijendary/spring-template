package com.leijendary.spring.boot.template.api.v1.controller;

import static com.leijendary.spring.boot.template.api.v1.service.SampleTableService.CACHE_KEY;
import static com.leijendary.spring.boot.template.util.LocationUtil.locationHeader;
import static com.leijendary.spring.boot.template.util.RequestContext.getLanguage;
import static com.leijendary.spring.boot.template.util.RequestContext.getLocale;
import static com.leijendary.spring.boot.template.util.RequestContext.getTimeZone;
import static com.leijendary.spring.boot.template.util.RequestContext.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.leijendary.spring.boot.template.api.v1.data.SampleRequest;
import com.leijendary.spring.boot.template.api.v1.data.SampleResponse;
import com.leijendary.spring.boot.template.api.v1.service.SampleTableService;
import com.leijendary.spring.boot.template.client.SampleClient;
import com.leijendary.spring.boot.template.data.DataResponse;
import com.leijendary.spring.boot.template.data.QueryRequest;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

;

/**
 * This is an example of a controller that will be created in microservices.
 *
 * There are 3 parts of the {@link RequestMapping} url that we need to take note
 * of: 1. The api prefix ("api") 2. The version ("v1") 3. The parent path of
 * this API ("/") which can be anything that this specific controller should be
 * doing.
 *
 * Since this microservice uses a context path, the result of the url should be
 * "/sample/api/v1"
 *
 * The url paths should be in kebab-case except for the query parameters, body,
 * and other URL parts in which they should be in camelCase.
 *
 * For headers, I would recommend that the Header keys should be in
 * Pascal-Kebab-Case
 */
@RestController
@RequestMapping("/api/v1/samples")
@RequiredArgsConstructor
@Api("This is just a sample controller with a swagger documentation")
public class SampleController {

    private final SampleClient sampleClient;
    private final SampleTableService sampleTableService;

    /**
     * This is a sample RequestMapping (Only GET method, that is why I used
     * {@link GetMapping})
     *
     * @param pageable The page request. Since this API has pageable results, it is
     *                 recommended that the request parameters contains
     *                 {@link Pageable}
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_urn:sample:list:v1')")
    @ApiOperation("Sample implementation of swagger in a api")
    public DataResponse<List<SampleResponse>> list(final QueryRequest queryRequest, final Pageable pageable) {
        final var page = sampleTableService.list(queryRequest, pageable);

        return DataResponse.<List<SampleResponse>>builder()
                .data(page.getContent())
                .meta(page)
                .links(page)
                .object(SampleResponse.class)
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_urn:sample:create:v1')")
    @CachePut(value = CACHE_KEY, key = "#result.data.id")
    @ResponseStatus(CREATED)
    @ApiOperation("Saves a sample record into the database")
    public DataResponse<SampleResponse> create(@Valid @RequestBody final SampleRequest request,
            final HttpServletResponse httpServletResponse) {
        final var sampleResponse = sampleTableService.create(request);
        final var response = DataResponse.<SampleResponse>builder()
                .data(sampleResponse)
                .status(CREATED)
                .object(SampleResponse.class)
                .build();

        locationHeader(httpServletResponse, sampleResponse.getId());

        return response;
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('SCOPE_urn:sample:get:v1')")
    @Cacheable(value = CACHE_KEY, key = "#id")
    @ApiOperation("Retrieves the sample record from the database")
    public DataResponse<SampleResponse> get(@PathVariable final UUID id) {
        final var sampleResponse = sampleTableService.get(id);

        return DataResponse.<SampleResponse>builder()
                .data(sampleResponse)
                .object(SampleResponse.class)
                .build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('SCOPE_urn:sample:update:v1')")
    @CachePut(value = CACHE_KEY, key = "#result.data.id")
    @ApiOperation("Updates the sample record into the database")
    public DataResponse<SampleResponse> update(@PathVariable final UUID id,
            @Valid @RequestBody final SampleRequest request) {
        final var sampleResponse = sampleTableService.update(id, request);

        return DataResponse.<SampleResponse>builder()
                .data(sampleResponse)
                .object(SampleResponse.class)
                .build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('SCOPE_urn:sample:delete:v1')")
    @CacheEvict(value = CACHE_KEY, key = "#id")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Removes the sample record from the database")
    public void delete(@PathVariable final UUID id) {
        sampleTableService.delete(id);
    }

    @GetMapping(value = "client", produces = TEXT_HTML_VALUE)
    public String client() {
        return sampleClient.homepage();
    }

    @GetMapping(value = "timezone", produces = TEXT_PLAIN_VALUE)
    public String timezone() {
        final var timeZone = getTimeZone();
        final var zoneId = timeZone.toZoneId();
        final var displayName = zoneId.getDisplayName(TextStyle.FULL, getLocale());
        final var id = zoneId.getId();

        return String.format("%s %s", displayName, id);
    }

    @GetMapping(value = "locale", produces = TEXT_PLAIN_VALUE)
    public String locale() {
        return getLocale().toString();
    }

    @GetMapping(value = "language", produces = TEXT_PLAIN_VALUE)
    public String language() {
        return getLanguage();
    }

    @GetMapping("timestamp")
    public Map<String, OffsetDateTime> timestamp() {
        final var map = new HashMap<String, OffsetDateTime>();
        map.put("current", now());

        return map;
    }
}
