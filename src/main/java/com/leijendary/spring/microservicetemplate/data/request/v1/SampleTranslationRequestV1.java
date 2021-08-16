package com.leijendary.spring.microservicetemplate.data.request.v1;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class SampleTranslationRequestV1 {

    @NotBlank(message = "validation.required")
    private String name;

    @NotBlank(message = "validation.required")
    private String description;

    @NotBlank(message = "validation.required")
    private String language;

    @Min(message = "validation.min", value = 1)
    private int ordinal;
}
