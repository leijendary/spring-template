package com.leijendary.spring.microservicetemplate.data.response;

import com.leijendary.spring.microservicetemplate.data.PageMeta;
import com.leijendary.spring.microservicetemplate.util.RequestContextUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataResponse<T> {

    private T data;
    private Map<String, Object> meta;
    private Map<String, URI> links;

    public static <T> DataResponseBuilder<T> builder() {
        return new DataResponseBuilder<T>()
                .status(OK)
                .selfLink();
    }

    public static class DataResponseBuilder<T> {

        private T data;
        private final Map<String, Object> meta = new HashMap<>();
        private final Map<String, URI> links = new HashMap<>();

        public DataResponse<T> build() {
            this.meta.put("timestamp", Instant.now());

            if (!this.meta.containsKey("type")) {
                throw new IllegalArgumentException("Type is not indicated");
            }

            if (!this.meta.containsKey("object")) {
                throw new IllegalArgumentException("Object is not indicated");
            }

            return new DataResponse<>(data, meta, links);
        }

        public DataResponseBuilder<T> data(final T data) {
            this.data = data;

            if (data instanceof Iterable) {
                this.meta.put("type", "array");
            } else {
                this.meta.put("type", "object");
            }

            return this;
        }

        public DataResponseBuilder<T> object(final String object) {
            this.meta.put("object", object);

            return this;
        }

        public DataResponseBuilder<T> meta(final String key, final Object value) {
            this.meta.put(key, value);

            return this;
        }

        public DataResponseBuilder<T> status(final HttpStatus httpStatus) {
            this.meta.put("status", httpStatus.value());

            return this;
        }

        public DataResponseBuilder<T> meta(final Page<?> page) {
            this.meta.put("page", new PageMeta(page));

            return this;
        }

        public DataResponseBuilder<T> selfLink() {
            this.links.put("self", RequestContextUtil.uri());

            return this;
        }

        public DataResponseBuilder<T> links(final Page<?> page) {
            final var size = page.getSize();
            final var sort = page.getSort();

            this.links.put("self", createLink(page.getPageable().getPageNumber(), size, sort));

            if (page.hasPrevious()) {
                this.links.put("previous", createLink(page.previousOrFirstPageable().getPageNumber(), size, sort));
            }

            if (page.hasNext()) {
                this.links.put("next", createLink(page.nextOrLastPageable().getPageNumber(), size, sort));
            }

            this.links.put("last", createLink(page.getTotalPages() - 1, size, sort));

            return this;
        }

        private URI createLink(final int page, final int size, final Sort sort) {
            final var uri = UriComponentsBuilder.fromUri(RequestContextUtil.uri());
            uri.replaceQueryParam("page", page + 1);
            uri.replaceQueryParam("size", size);

            if (sort.isSorted()) {
                uri.replaceQueryParam("sort", sort.toString());
            }

            return uri.build().toUri();
        }
    }
}
