package com.leijendary.error

import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorSource
import com.leijendary.util.requestContext
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

private val SOURCE = ErrorSource(pointer = "/path")

@RestControllerAdvice
@Order(1)
class NoResourceFoundExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(NoResourceFoundException::class)
    @ResponseStatus(NOT_FOUND)
    fun catchNoResourceFound(exception: NoResourceFoundException): List<ErrorModel> {
        val code = "error.mapping.notFound"
        val arguments = arrayOf(exception.httpMethod.name(), "/${exception.resourcePath}")
        val message = messageSource.getMessage(code, arguments, requestContext.locale)
        val error = ErrorModel(code = code, message = message, source = SOURCE)

        return listOf(error)
    }
}
