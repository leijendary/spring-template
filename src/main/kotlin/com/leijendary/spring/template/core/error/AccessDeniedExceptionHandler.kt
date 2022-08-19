package com.leijendary.spring.template.core.error

import com.leijendary.spring.template.core.data.ErrorResponse
import com.leijendary.spring.template.core.util.RequestContext
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(1)
class AccessDeniedExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(AccessDeniedException::class)
    @ResponseStatus(FORBIDDEN)
    fun catchAccessDenied(exception: AccessDeniedException): ErrorResponse {
        val code = "access.denied"
        val arguments = arrayOfNulls<Any>(0)
        val message = messageSource.getMessage(code, arguments, RequestContext.locale)

        return ErrorResponse.builder()
            .addError(mutableListOf("header", "authorization"), code, message + ": " + exception.message)
            .status(FORBIDDEN)
            .build()
    }
}