package com.leijendary.error

import com.leijendary.error.exception.StatusException
import com.leijendary.model.ErrorModel
import com.leijendary.util.RequestContext.locale
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(3)
class StatusExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(StatusException::class)
    fun catchStatus(exception: StatusException): ResponseEntity<List<ErrorModel>> {
        val message = messageSource.getMessage(exception.code, exception.arguments, locale)
        val error = ErrorModel(code = exception.code, message = message, source = exception.source)
        val errors = listOf(error)

        return ResponseEntity
            .status(exception.status)
            .body(errors)
    }
}
