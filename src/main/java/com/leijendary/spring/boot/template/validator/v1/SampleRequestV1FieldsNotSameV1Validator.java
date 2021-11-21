package com.leijendary.spring.boot.template.validator.v1;

import com.leijendary.spring.boot.template.api.v1.data.SampleRequest;
import com.leijendary.spring.boot.template.validator.annotation.v1.FieldsNotEqualV1;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Optional.ofNullable;

public class SampleRequestV1FieldsNotSameV1Validator implements ConstraintValidator<FieldsNotEqualV1, SampleRequest> {

    @Override
    public void initialize(final FieldsNotEqualV1 constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final SampleRequest value, final ConstraintValidatorContext context) {
        return ofNullable(value.getField1())
                .map(field1 -> !field1.equals(String.valueOf(value.getField2())))
                .orElse(true);
    }
}
