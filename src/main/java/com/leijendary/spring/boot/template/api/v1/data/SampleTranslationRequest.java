package com.leijendary.spring.boot.template.api.v1.data;

import javax.validation.constraints.NotBlank;

import com.leijendary.spring.boot.template.data.TranslationRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleTranslationRequest extends TranslationRequest {

    @NotBlank(message = "validation.required")
    private String name;

    @NotBlank(message = "validation.required")
    private String description;
}
