package com.leijendary.spring.microservicetemplate.data.request.v1;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SampleRequestV1 {

    @NotBlank(message = "validation.required")
    @Size(max = 50, message = "validation.maxLength")
    private String field1;

    private int field2;
}
