package com.leijendary.spring.microservicetemplate.data.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServerWebInputResponse implements ErrorResponse {

    private final String error;
    private final String message;
    private final int status;

    @Override
    public int getStatus() {
        return this.status;
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
