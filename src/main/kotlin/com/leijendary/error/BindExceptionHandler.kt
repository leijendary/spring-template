package com.leijendary.error

import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorSource
import com.leijendary.util.requestContext
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

private const val CODE_BINDING_INVALID_VALUE = "validation.binding.invalidValue"

@RestControllerAdvice
@Order(4)
class BindExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(BindException::class)
    @ResponseStatus(BAD_REQUEST)
    fun catchBind(exception: BindException): List<ErrorModel> {
        return exception.allErrors.map(::map)
    }

    private fun map(error: ObjectError): ErrorModel {
        val (prefix, field, isBindingFailure) = if (error is FieldError) {
            val prefix = if (!error.isBindingFailure) "/body/" else ""

            Triple(prefix, error.field, error.isBindingFailure)
        } else {
            Triple("", error.objectName, true)
        }
        val location = field.split(".", "[", "]")
            .stream()
            .filter { it.isNotBlank() }
            .map { it.toIntOrNull() ?: it }
            .toArray()
            .joinToString("/", prefix = prefix)
        val source = if (isBindingFailure) ErrorSource(parameter = location) else ErrorSource(pointer = location)
        val code = if (!error.shouldRenderDefaultMessage()) {
            error.defaultMessage ?: CODE_BINDING_INVALID_VALUE
        } else {
            CODE_BINDING_INVALID_VALUE
        }
        val message = messageSource.getMessage(code, error.arguments, code, requestContext.locale)

        return ErrorModel(code = code, message = message, source = source)
    }
}
