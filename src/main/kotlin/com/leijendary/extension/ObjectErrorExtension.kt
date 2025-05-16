package com.leijendary.extension

import com.leijendary.context.RequestContext.locale
import com.leijendary.error.CODE_BINDING_INVALID_VALUE
import com.leijendary.model.ErrorModel
import org.springframework.context.MessageSource
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError

fun List<ObjectError>.toErrorModels(messageSource: MessageSource?): List<ErrorModel> {
    return map { it.toErrorModel(messageSource) }
}

fun ObjectError.toErrorModel(messageSource: MessageSource?): ErrorModel {
    val (field, isBindingFailure) = if (this is FieldError) field to isBindingFailure else objectName to true
    val prefix = if (isBindingFailure) "#/parameter/" else "#/body/"
    val pointer = field.split(".", "[", "]")
        .stream()
        .filter { it.isNotBlank() }
        .toArray()
        .joinToString("/", prefix)
    val code = if (!shouldRenderDefaultMessage()) {
        this.defaultMessage ?: CODE_BINDING_INVALID_VALUE
    } else {
        CODE_BINDING_INVALID_VALUE
    }
    val message = messageSource?.getMessage(code, arguments, code, locale)

    return ErrorModel(code, message, pointer)
}
