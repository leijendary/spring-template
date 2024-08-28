package com.leijendary.error

import com.leijendary.context.RequestContext
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.model.ErrorModel
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(3)
class ResourceNotFoundExceptionHandler(
    private val messageSource: MessageSource,
    private val requestContext: RequestContext
) {
    @ExceptionHandler(ResourceNotFoundException::class)
    @ResponseStatus(NOT_FOUND)
    fun catchResourceNotFound(exception: ResourceNotFoundException): List<ErrorModel> {
        val code = "error.resource.notFound"
        val arguments = arrayOf(exception.entity)
        val message = messageSource.getMessage(code, arguments, requestContext.locale)
        val error = ErrorModel(id = exception.id.toString(), code = code, message = message, source = exception.source)

        return listOf(error)
    }
}
