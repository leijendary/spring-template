package com.leijendary.validator.annotation

import com.leijendary.error.CODE_DUPLICATE_VALUE
import com.leijendary.validator.UniqueFieldsValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.reflect.KClass

@Target(FIELD, PROPERTY_SETTER)
@Retention
@Constraint(validatedBy = [UniqueFieldsValidator::class])
annotation class UniqueFields(
    val fields: Array<String>,
    val message: String = CODE_DUPLICATE_VALUE,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
