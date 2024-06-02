package com.leijendary.error

import com.leijendary.error.exception.ResourceNotUniqueException
import com.leijendary.model.ErrorModel
import com.leijendary.util.requestContext
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(3)
class ResourceNotUniqueExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(ResourceNotUniqueException::class)
    @ResponseStatus(CONFLICT)
    fun catchResourceNotUnique(exception: ResourceNotUniqueException): List<ErrorModel> {
        val code = "validation.alreadyExists"
        val source = exception.source
        val field = source.pointer?.split("/")?.last()
        val arguments = arrayOf(field, exception.value)
        val message = messageSource.getMessage(code, arguments, requestContext.locale)
        val error = ErrorModel(id = exception.value.toString(), code = code, message = message, source = source)

        return listOf(error)
    }
}
