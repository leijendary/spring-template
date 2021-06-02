package com.leijendary.spring.microservicetemplate.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorData {

    private String source;
    private String code;
    private String message;
}
