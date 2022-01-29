package com.leijendary.spring.boot.template.core.error

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonMappingException.Reference
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.leijendary.spring.boot.template.core.data.ErrorResponse
import com.leijendary.spring.boot.template.core.data.ErrorResponse.ErrorResponseBuilder
import com.leijendary.spring.boot.template.core.util.RequestContext.locale
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(2)
class HttpMessageNotReadableExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(BAD_REQUEST)
    fun catchHttpMessageNotReadable(exception: HttpMessageNotReadableException): ErrorResponse {
        val response: ErrorResponseBuilder = ErrorResponse.builder().status(BAD_REQUEST)

        errors(exception, response)

        return response.build()
    }

    private fun errors(exception: HttpMessageNotReadableException, errorResponse: ErrorResponseBuilder) {
        val source = mutableListOf("body")
        val code = "error.badRequest"
        var message = exception.message ?: ""

        if (message.startsWith("Required request body is missing")) {
            errorResponse.addError(source, code, message.split(":".toRegex()).toTypedArray()[0])

            return
        }

        if (exception.cause is InvalidFormatException) {
            errors(exception.cause as InvalidFormatException, errorResponse)

            return
        }

        if (exception.cause is JsonMappingException) {
            errors(exception.cause as JsonMappingException, errorResponse)

            return
        }

        message = exception.message?.replace("JSON decoding error: ", "") ?: ""

        errorResponse.addError(source, code, message)
    }

    private fun errors(exception: InvalidFormatException, errorResponse: ErrorResponseBuilder) {
        val source = createSource(exception.path)
        val code = "error.invalid.format"
        val arguments = arrayOf(source, exception.value, exception.targetType.simpleName)
        val message = messageSource.getMessage(code, arguments, locale)

        errorResponse.addError(source, code, message)
    }

    private fun errors(exception: JsonMappingException, errorResponse: ErrorResponseBuilder) {
        val source = createSource(exception.path)
        val message: String = exception.originalMessage

        errorResponse.addError(source, "error.invalid.format", message)
    }

    private fun createSource(path: List<Reference>): List<Any> {
        return listOf("body") + path.map { listOf(it.index, it.fieldName) }
    }
}