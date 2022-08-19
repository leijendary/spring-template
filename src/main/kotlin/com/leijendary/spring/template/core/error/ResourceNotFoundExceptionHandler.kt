package com.leijendary.spring.template.core.error

import com.leijendary.spring.template.core.data.ErrorResponse
import com.leijendary.spring.template.core.exception.ResourceNotFoundException
import com.leijendary.spring.template.core.util.RequestContext.locale
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(3)
class ResourceNotFoundExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(ResourceNotFoundException::class)
    @ResponseStatus(NOT_FOUND)
    fun catchResourceNotFound(exception: ResourceNotFoundException): ErrorResponse {
        val source = exception.source
        val code = "error.resource.notFound"
        val arguments = arrayOf(source.joinToString(separator = "."), exception.identifier)
        val message = messageSource.getMessage(code, arguments, locale)

        return ErrorResponse.builder()
            .addError(source, code, message)
            .status(NOT_FOUND)
            .build()
    }
}