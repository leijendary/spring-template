package com.leijendary.spring.boot.template.core.error

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonMappingException.Reference
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.leijendary.spring.boot.template.core.data.ErrorData
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
    fun catchHttpMessageNotReadable(exception: HttpMessageNotReadableException): List<ErrorData> {
        val errorData = errors(exception)

        return listOf(errorData)
    }

    private fun errors(exception: HttpMessageNotReadableException): ErrorData {
        val source = mutableListOf("body")
        val code = "error.badRequest"
        var message = exception.message ?: ""

        if (message.startsWith("Required request body is missing")) {
            return ErrorData(source, code, message.split(":".toRegex()).toTypedArray()[0])
        }

        if (exception.cause is InvalidFormatException) {
            return errors(exception.cause as InvalidFormatException)
        }

        if (exception.cause is JsonMappingException) {
            return errors(exception.cause as JsonMappingException)
        }

        message = exception.message?.replace("JSON decoding error: ", "") ?: ""

        return ErrorData(source, code, message)
    }

    private fun errors(exception: InvalidFormatException): ErrorData {
        val source = createSource(exception.path)
        val code = "error.invalid.format"
        val arguments = arrayOf(source, exception.value, exception.targetType.simpleName)
        val message = messageSource.getMessage(code, arguments, locale)

        return ErrorData(source, code, message)
    }

    private fun errors(exception: JsonMappingException): ErrorData {
        val source = createSource(exception.path)
        val message: String = exception.originalMessage

        return ErrorData(source, "error.invalid.format", message)
    }

    private fun createSource(path: List<Reference>): List<Any> {
        return listOf("body") + path.map { listOf(it.index, it.fieldName) }
    }
}