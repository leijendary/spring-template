package com.leijendary.error

import com.leijendary.context.RequestContext.locale
import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorSource
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(2)
class MissingRequestHeaderExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(MissingRequestHeaderException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun catchMissingRequestHeader(exception: MissingRequestHeaderException): List<ErrorModel> {
        val code = "error.badRequest"
        val message = messageSource.getMessage(code, null, locale)
        val source = ErrorSource(header = exception.headerName)
        val errorModel = ErrorModel(code = code, message = message, source = source)

        return listOf(errorModel)
    }
}
