package com.leijendary.spring.microservicetemplate.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldValidationError {

    private String field;
    private String detail;
}
