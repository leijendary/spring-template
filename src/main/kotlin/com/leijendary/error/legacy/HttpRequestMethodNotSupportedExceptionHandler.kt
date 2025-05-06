package com.leijendary.error.legacy

import com.leijendary.context.RequestContext.isApi
import com.leijendary.context.RequestContext.locale
import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorModelsResponse
import com.leijendary.model.ErrorSource
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

private val SOURCE = ErrorSource(pointer = "/method")

// @ControllerAdvice
@Order(4)
class HttpRequestMethodNotSupportedExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ErrorModelsResponse(status = "405", description = "Request method is not supported.")
    fun catchHttpRequestMethodNotSupported(exception: HttpRequestMethodNotSupportedException): Any {
        if (!isApi) {
            return "404"
        }

        val code = "error.method.notSupported"
        val arguments = arrayOf(exception.method, exception.supportedHttpMethods)
        val message = messageSource.getMessage(code, arguments, locale)
        val error = ErrorModel(code = code, message = message, source = SOURCE)

        return ResponseEntity.status(METHOD_NOT_ALLOWED).body(listOf(error))
    }
}
