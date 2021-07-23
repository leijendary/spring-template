package com.leijendary.spring.microservicetemplate.validator.v1;

import com.leijendary.spring.microservicetemplate.data.request.v1.SampleRequestV1;
import com.leijendary.spring.microservicetemplate.validator.annotation.FieldsNotSame;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Optional.ofNullable;

public class SampleRequestV1FieldsNotSameV1Validator implements ConstraintValidator<FieldsNotSame, SampleRequestV1> {

    @Override
    public void initialize(FieldsNotSame constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SampleRequestV1 value, ConstraintValidatorContext context) {
        return ofNullable(value.getField1())
                .map(field1 -> !field1.equals(String.valueOf(value.getField2())))
                .orElse(true);
    }
}
