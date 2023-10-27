package com.leijendary.spring.core.error

import com.leijendary.spring.core.extension.logger
import com.leijendary.spring.core.model.ErrorModel
import com.leijendary.spring.core.model.ErrorSource
import com.leijendary.spring.core.util.RequestContext.locale
import com.leijendary.spring.core.util.SpringContext.Companion.isProd
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

private const val CODE = "error.serverError"
private val SOURCE = ErrorSource(pointer = "/server/internal")

@RestControllerAdvice
@Order
class GlobalExceptionHandler(messageSource: MessageSource) {
    private val log = logger()
    private val message = messageSource.getMessage(CODE, emptyArray(), locale)

    @ExceptionHandler(Exception::class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    fun catchException(exception: Exception): List<ErrorModel> {
        log.error("Global Exception", exception)

        val message = if (isProd()) this.message else exception.message
        val error = ErrorModel(code = CODE, message = message, source = SOURCE)

        return listOf(error)
    }
}
