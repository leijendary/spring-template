package com.leijendary.spring.core.error

import com.leijendary.spring.core.exception.ResourceNotFoundException
import com.leijendary.spring.core.model.ErrorModel
import com.leijendary.spring.core.util.RequestContext.locale
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
    fun catchResourceNotFound(exception: ResourceNotFoundException): List<ErrorModel> {
        val code = "error.resource.notFound"
        val message = messageSource.getMessage(code, emptyArray(), locale)
        val error = ErrorModel(id = exception.id.toString(), code = code, message = message, source = exception.source)

        return listOf(error)
    }
}
