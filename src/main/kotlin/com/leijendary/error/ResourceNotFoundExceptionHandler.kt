package com.leijendary.error

import com.leijendary.context.RequestContext.isApi
import com.leijendary.context.RequestContext.locale
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorModelsResponse
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

// @ControllerAdvice
@Order(3)
class ResourceNotFoundExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(ResourceNotFoundException::class)
    @ResponseStatus(NOT_FOUND)
    @ErrorModelsResponse(status = "404", description = "Resource not found.")
    fun catchResourceNotFound(exception: ResourceNotFoundException): Any {
        if (!isApi) {
            return "404"
        }

        val code = "error.resource.notFound"
        val arguments = arrayOf(exception.entity)
        val message = messageSource.getMessage(code, arguments, locale)
        val error = ErrorModel(id = exception.id.toString(), code = code, message = message, source = exception.source)

        return listOf(error)
    }
}
