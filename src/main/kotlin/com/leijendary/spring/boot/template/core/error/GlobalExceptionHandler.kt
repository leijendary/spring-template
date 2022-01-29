package com.leijendary.spring.boot.template.core.error

import com.leijendary.spring.boot.template.core.data.ErrorResponse
import com.leijendary.spring.boot.template.core.util.RequestContext.locale
import com.leijendary.spring.boot.template.core.util.logger
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order
class GlobalExceptionHandler(private val messageSource: MessageSource) {
    val log = logger()

    @ExceptionHandler(Exception::class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    fun catchException(exception: Exception): ErrorResponse {
        log.error("Global Exception", exception)

        val code = "error.generic"
        val arguments = arrayOfNulls<Any>(0)
        val error = messageSource.getMessage(code, arguments, locale)

        return ErrorResponse.builder()
            .addError(mutableListOf(error), code, exception.message)
            .build()
    }
}