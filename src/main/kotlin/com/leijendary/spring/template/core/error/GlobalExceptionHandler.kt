package com.leijendary.spring.template.core.error

import com.leijendary.spring.template.core.data.ErrorResponse
import com.leijendary.spring.template.core.extension.logger
import com.leijendary.spring.template.core.util.RequestContext.locale
import com.leijendary.spring.template.core.util.SpringContext.Companion.isProd
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order
class GlobalExceptionHandler(private val messageSource: MessageSource) {
    private val log = logger()

    @ExceptionHandler(Exception::class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    fun catchException(exception: Exception): ErrorResponse {
        log.error("Global Exception", exception)

        val code = "error.serverError"
        val message = if (isProd()) {
            messageSource.getMessage(code, emptyArray(), locale)
        } else {
            exception.message
        }

        return ErrorResponse.builder()
            .addError(mutableListOf("server", "internal"), code, message)
            .build()
    }
}