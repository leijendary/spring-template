package com.leijendary.spring.boot.template.data.request.v1;

import javax.validation.constraints.NotBlank;

import com.leijendary.spring.boot.template.data.request.TranslationRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleTranslationRequestV1 extends TranslationRequest {

    @NotBlank(message = "validation.required")
    private String name;

    @NotBlank(message = "validation.required")
    private String description;
}
