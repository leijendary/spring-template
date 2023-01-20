package com.leijendary.spring.template.core.error

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonMappingException.Reference
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.leijendary.spring.template.core.data.ErrorData
import com.leijendary.spring.template.core.util.RequestContext.locale
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
    fun catchHttpMessageNotReadable(exception: HttpMessageNotReadableException) = errors(exception)

    private fun errors(exception: HttpMessageNotReadableException): List<ErrorData> {
        val source = mutableListOf("body")
        val code = "error.badRequest"
        var message = exception.message ?: ""
        val error: ErrorData

        if (message.startsWith("Required request body is missing")) {
            error = ErrorData(source, code, message.split(":".toRegex()).toTypedArray()[0])

            return listOf(error)
        }

        when (val cause = exception.cause) {
            is InvalidFormatException -> errors(cause)
            is JsonMappingException -> errors(cause)
        }

        message = exception.message?.replace("JSON decoding error: ", "") ?: ""
        error = ErrorData(source, code, message)

        return listOf(error)
    }

    private fun errors(exception: InvalidFormatException): List<ErrorData> {
        val source = createSource(exception.path)
        val code = "error.body.format.invalid"
        val arguments = arrayOf(source, exception.value, exception.targetType.simpleName)
        val message = messageSource.getMessage(code, arguments, locale)
        val error = ErrorData(source, code, message)

        return listOf(error)
    }

    private fun errors(exception: JsonMappingException): List<ErrorData> {
        val source = createSource(exception.path)
        val message: String = exception.originalMessage
        val error = ErrorData(source, "error.body.format.invalid", message)

        return listOf(error)
    }

    private fun createSource(path: List<Reference>): List<Any> {
        return listOf("body") + path.map { listOf(it.index, it.fieldName) }
    }
}
