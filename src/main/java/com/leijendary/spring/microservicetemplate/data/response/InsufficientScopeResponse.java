package com.leijendary.spring.microservicetemplate.data.response;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class InsufficientScopeResponse extends AccessDeniedResponse {

    @Getter
    private final Set<String> scopes;

    public InsufficientScopeResponse(String error, String message, String[] scopes) {
        super(error, message);

        this.scopes = new HashSet<>(asList(scopes));
    }
}
