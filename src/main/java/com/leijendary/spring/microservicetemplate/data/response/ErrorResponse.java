package com.leijendary.spring.microservicetemplate.data.response;

import com.leijendary.spring.microservicetemplate.util.RequestContextUtil;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.Instant;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

public interface ErrorResponse {

    default Instant getTimestamp() {
        return Instant.now();
    }

    default String getPath() {
        return RequestContextUtil.getPath();
    }

    int getStatus();

    String getError();

    String getMessage();
}
