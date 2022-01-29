package com.leijendary.spring.boot.template.core.error

import com.leijendary.spring.boot.template.core.data.ErrorResponse
import com.leijendary.spring.boot.template.core.exception.ResourceNotUniqueException
import com.leijendary.spring.boot.template.core.util.RequestContext.locale
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(3)
class ResourceNotUniqueExceptionHandler {
    private val messageSource: MessageSource? = null

    @ExceptionHandler(ResourceNotUniqueException::class)
    @ResponseStatus(CONFLICT)
    fun catchResourceNotUnique(exception: ResourceNotUniqueException): ErrorResponse {
        val source = exception.source;
        val code = "validation.alreadyExists"
        val arguments = arrayOf<Any>(source.joinToString(separator = "."), exception.value)
        val message = messageSource!!.getMessage(code, arguments, locale)

        return ErrorResponse.builder()
            .addError(source, code, message)
            .status(CONFLICT)
            .build()
    }
}