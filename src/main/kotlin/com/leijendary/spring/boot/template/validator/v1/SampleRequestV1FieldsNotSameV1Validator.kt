package com.leijendary.spring.boot.template.validator.v1

import com.leijendary.spring.boot.template.api.v1.data.SampleRequest
import com.leijendary.spring.boot.template.validator.annotation.v1.FieldsNotEqualV1
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class SampleRequestV1FieldsNotSameV1Validator : ConstraintValidator<FieldsNotEqualV1, SampleRequest> {

    override fun initialize(constraintAnnotation: FieldsNotEqualV1) {
        super.initialize(constraintAnnotation)
    }

    override fun isValid(value: SampleRequest, context: ConstraintValidatorContext): Boolean {
        return value.field1?.let { it != value.field2.toString() } ?: true
    }
}