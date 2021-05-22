package com.leijendary.spring.microservicetemplate.data.response;

import lombok.AllArgsConstructor;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@AllArgsConstructor
public class ValidationResponse implements ErrorResponse {

    private final String error;
    private final String message;
    private final List<FieldValidationError> validations;

    @Override
    public int getStatus() {
        return BAD_REQUEST.value();
    }

    @Override
    public String getError() {
        return this.error;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public List<FieldValidationError> getValidations() {
        return this.validations;
    }
}
