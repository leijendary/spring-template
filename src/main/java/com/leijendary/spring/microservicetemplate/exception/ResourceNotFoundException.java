package com.leijendary.spring.microservicetemplate.exception;

import lombok.Getter;

import static java.lang.String.format;

public class ResourceNotFoundException extends RuntimeException {

    @Getter
    private final String resource;

    @Getter
    private final Object identifier;

    public ResourceNotFoundException(String resource, Object identifier) {
        super(format("%s with identifier %s not found", resource, identifier));

        this.resource = resource;
        this.identifier = identifier;
    }
}
