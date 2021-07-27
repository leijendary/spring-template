package com.leijendary.spring.microservicetemplate.validator.annotation.v1;

import com.leijendary.spring.microservicetemplate.validator.v1.SampleRequestV1FieldsNotSameV1Validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SampleRequestV1FieldsNotSameV1Validator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsNotEqualV1 {

    String message() default "validation.fields.isSame";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
