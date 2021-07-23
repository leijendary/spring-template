package com.leijendary.spring.microservicetemplate.exception;

import lombok.Getter;

public class ResourceNotUniqueException extends RuntimeException {

    @Getter
    private final String field;

    @Getter
    private final String value;

    public ResourceNotUniqueException(final String field, final String value) {
        this.field = field;
        this.value = value;
    }
}
