package com.leijendary.validator.annotation

import com.leijendary.validator.UniqueFieldsValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

@Constraint(validatedBy = [UniqueFieldsValidator::class])
@Target(FIELD)
@Retention(RUNTIME)
annotation class UniqueFields(
    val message: String = "validation.duplicateValue",
    val uniqueFields: Array<String> = [],
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
