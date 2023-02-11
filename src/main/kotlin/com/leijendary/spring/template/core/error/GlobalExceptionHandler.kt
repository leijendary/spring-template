package com.leijendary.spring.template.core.error

import com.leijendary.spring.template.core.extension.logger
import com.leijendary.spring.template.core.model.ErrorModel
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
    fun catchException(exception: Exception): List<ErrorModel> {
        log.error("Global Exception", exception)

        val code = "error.serverError"
        val message = if (isProd()) {
            messageSource.getMessage(code, emptyArray(), locale)
        } else {
            exception.message
        }
        val error = ErrorModel(mutableListOf("server", "internal"), code, message)

        return listOf(error)
    }
}
