package com.leijendary.error

import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorSource
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException

@RestControllerAdvice
@Order(4)
class HandlerMethodValidationExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(HandlerMethodValidationException::class)
    @ResponseStatus(BAD_REQUEST)
    fun catchHandlerMethodValidation(exception: HandlerMethodValidationException): List<ErrorModel> {
        return exception.allValidationResults.flatMap { result ->
            val source = ErrorSource(parameter = result.methodParameter.parameterName ?: "")

            result.resolvableErrors.map {
                val code = it.defaultMessage ?: "error.badRequest"
                val message = messageSource.getMessage(code, it.arguments, LocaleContextHolder.getLocale())

                ErrorModel(code = code, message = message, source = source)
            }
        }
    }
}
