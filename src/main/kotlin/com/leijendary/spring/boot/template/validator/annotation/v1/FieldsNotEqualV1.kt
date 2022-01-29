package com.leijendary.spring.boot.template.validator.annotation.v1

import com.leijendary.spring.boot.template.validator.v1.SampleRequestV1FieldsNotSameV1Validator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [SampleRequestV1FieldsNotSameV1Validator::class])
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class FieldsNotEqualV1(
    val message: String = "validation.fields.isSame",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)