package com.leijendary.spring.microservicetemplate.data.request.v1;

import com.leijendary.spring.microservicetemplate.validator.annotation.v1.FieldsNotEqualV1;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldsNotEqualV1
public class SampleRequestV1 {

    @NotBlank(message = "validation.required")
    @Size(max = 50, message = "validation.maxLength")
    private String field1;

    private int field2;

    @Valid
    @NotEmpty(message = "validation.required")
    public Set<SampleTranslationRequestV1> translations = new HashSet<>();
}
