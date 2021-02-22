package com.leijendary.spring.microservicetemplate.data.response;

import lombok.AllArgsConstructor;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@AllArgsConstructor
public class GlobalExceptionResponse implements ErrorResponse {

    private final String error;
    private final String message;

    @Override
    public int getStatus() {
        return INTERNAL_SERVER_ERROR.value();
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
