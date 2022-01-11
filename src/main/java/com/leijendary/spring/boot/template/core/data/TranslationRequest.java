package com.leijendary.spring.boot.template.core.data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class TranslationRequest {

    @NotBlank(message = "validation.required")
    private String language;

    @Min(message = "validation.min", value = 1)
    private int ordinal;
}
