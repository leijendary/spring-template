package com.leijendary.spring.microservicetemplate.data.response;

import com.leijendary.spring.microservicetemplate.util.RequestContextUtil;

import java.time.Instant;

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
