package com.leijendary.error

import com.leijendary.context.RequestContext.locale
import com.leijendary.context.SpringContext.Companion.isProd
import com.leijendary.extension.logger
import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorSource
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

const val CODE_SERVER_ERROR = "error.serverError"
val SOURCE_SERVER_INTERNAL = ErrorSource(pointer = "/server/internal")

@RestControllerAdvice
@Order
class GlobalExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(Exception::class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    fun catchException(exception: Exception): List<ErrorModel> {
        log.error("Global Exception", exception)

        val message = if (isProd) messageSource.getMessage(CODE_SERVER_ERROR, null, locale) else exception.message
        val error = ErrorModel(code = CODE_SERVER_ERROR, message = message, source = SOURCE_SERVER_INTERNAL)

        return listOf(error)
    }

    companion object {
        private val log = logger()
    }
}
