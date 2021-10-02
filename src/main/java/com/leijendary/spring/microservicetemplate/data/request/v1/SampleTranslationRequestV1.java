package com.leijendary.spring.microservicetemplate.data.request.v1;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleTranslationRequestV1 extends TranslationRequestV1 {

    @NotBlank(message = "validation.required")
    private String name;

    @NotBlank(message = "validation.required")
    private String description;
}
