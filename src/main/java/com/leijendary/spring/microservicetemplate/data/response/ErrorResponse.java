package com.leijendary.spring.microservicetemplate.data.response;

import com.leijendary.spring.microservicetemplate.data.ErrorData;
import com.leijendary.spring.microservicetemplate.util.RequestContextUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private List<ErrorData> errors;
    private Map<String, Object> meta;
    private Map<String, URI> links;

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder()
                .status(INTERNAL_SERVER_ERROR)
                .selfLink();
    }

    public static class ErrorResponseBuilder {

        private final List<ErrorData> errors = new ArrayList<>();
        private final Map<String, Object> meta = new HashMap<>();
        private final Map<String, URI> links = new HashMap<>();

        public ErrorResponse build() {
            this.meta.put("timestamp", Instant.now());

            return new ErrorResponse(errors, meta, links);
        }

        public ErrorResponseBuilder addError(final String source, final String code, final String message) {
            this.errors.add(new ErrorData(source, code, message));

            return this;
        }

        public ErrorResponseBuilder meta(final String key, final Object value) {
            this.meta.put(key, value);

            return this;
        }

        public ErrorResponseBuilder status(HttpStatus httpStatus) {
            this.meta.put("status", httpStatus.value());

            return this;
        }

        public ErrorResponseBuilder selfLink() {
            this.links.put("self", RequestContextUtil.uri());

            return this;
        }
    }
}
