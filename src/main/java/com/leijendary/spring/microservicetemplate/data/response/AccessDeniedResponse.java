package com.leijendary.spring.microservicetemplate.data.response;

import lombok.AllArgsConstructor;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@AllArgsConstructor
public class AccessDeniedResponse implements ErrorResponse {

    private final String error;
    private final String message;

    @Override
    public int getStatus() {
        return FORBIDDEN.value();
    }

    @Override
    public String getError() {
        return this.error;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
