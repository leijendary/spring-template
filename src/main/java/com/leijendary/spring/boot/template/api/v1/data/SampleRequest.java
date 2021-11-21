package com.leijendary.spring.boot.template.api.v1.data;

import com.leijendary.spring.boot.template.validator.annotation.v1.FieldsNotEqualV1;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldsNotEqualV1
public class SampleRequest {

    @NotBlank(message = "validation.required")
    @Size(max = 50, message = "validation.maxLength")
    private String field1;

    private int field2;

    @Valid
    @NotEmpty(message = "validation.required")
    public Set<SampleTranslationRequest> translations = new HashSet<>();
}
