package com.leijendary.spring.microservicetemplate.data.response;

import lombok.AllArgsConstructor;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@AllArgsConstructor
public class AuthenticationExceptionResponse implements ErrorResponse {

    private final String error;
    private final String message;

    @Override
    public int getStatus() {
        return UNAUTHORIZED.value();
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
