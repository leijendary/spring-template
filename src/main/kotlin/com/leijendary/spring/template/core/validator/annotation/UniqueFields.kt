package com.leijendary.spring.template.core.validator.annotation

import com.leijendary.spring.template.core.validator.UniqueFieldsValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Constraint(validatedBy = [UniqueFieldsValidator::class])
@Target(ANNOTATION_CLASS, CLASS, PROPERTY_GETTER, FIELD)
@Retention(RUNTIME)
annotation class UniqueFields(
    val message: String = "validation.duplicateValue",
    val uniqueFields: Array<String> = [],
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)