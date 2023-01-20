package com.leijendary.spring.template.core.validator.annotation

import com.leijendary.spring.template.core.validator.EnumFieldValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

@Constraint(validatedBy = [EnumFieldValidator::class])
@Target(FIELD)
@Retention(RUNTIME)
annotation class EnumField(
    val enumClass: KClass<out Enum<*>>,
    val message: String = "validation.invalidValue",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
