package com.leijendary.error.legacy

import com.leijendary.context.RequestContext.locale
import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorSource
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

private const val CODE_BINDING_INVALID_VALUE = "validation.binding.invalidValue"

// @RestControllerAdvice
@Order(4)
class MethodExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(BindException::class)
    @ResponseStatus(BAD_REQUEST)
    fun catchBind(exception: BindException): List<ErrorModel> {
        return exception.allErrors.map(::map)
    }

    @ExceptionHandler(HandlerMethodValidationException::class)
    @ResponseStatus(BAD_REQUEST)
    fun catchHandlerMethodValidation(exception: HandlerMethodValidationException): List<ErrorModel> {
        return exception.parameterValidationResults.flatMap { result ->
            val source = ErrorSource(parameter = result.methodParameter.parameterName ?: "")

            result.resolvableErrors.map {
                if (it is ObjectError) {
                    map(it)
                } else {
                    val code = it.defaultMessage ?: "error.badRequest"
                    val message = messageSource.getMessage(code, it.arguments, locale)

                    ErrorModel(code = code, message = message, source = source)
                }
            }
        }
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(BAD_REQUEST)
    fun catchMethodArgumentTypeMismatch(exception: MethodArgumentTypeMismatchException): List<ErrorModel> {
        val message = messageSource.getMessage(CODE_BINDING_INVALID_VALUE, arrayOf(), locale)
        val source = ErrorSource(parameter = exception.name)
        val error = ErrorModel(code = CODE_BINDING_INVALID_VALUE, message = message, source = source)

        return listOf(error)
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
        val message = messageSource.getMessage(code, error.arguments, code, locale)

        return ErrorModel(code = code, message = message, source = source)
    }
}
