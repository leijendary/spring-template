package com.leijendary.error

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonMappingException.Reference
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorSource
import com.leijendary.util.requestContext
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

private val SOURCE = ErrorSource(pointer = "/body")

@RestControllerAdvice
@Order(2)
class HttpMessageNotReadableExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(BAD_REQUEST)
    fun catchHttpMessageNotReadable(exception: HttpMessageNotReadableException): List<ErrorModel> {
        val code = "error.badRequest"
        var message = exception.message?.split(":".toRegex())?.toTypedArray()?.get(0) ?: ""
        val error: ErrorModel

        if (message.startsWith("Required request body is missing")) {
            error = ErrorModel(code = code, message = message, source = SOURCE)

            return listOf(error)
        }

        error = when (val cause = exception.cause) {
            is InvalidFormatException -> error(cause)
            is MismatchedInputException -> error(cause)
            is JsonMappingException -> error(cause)
            else -> {
                message = exception.message?.replace("JSON decoding error: ", "") ?: ""

                ErrorModel(code = code, message = message, source = SOURCE)
            }
        }

        return listOf(error)
    }

    private fun error(exception: InvalidFormatException): ErrorModel {
        val source = source(exception.path)
        val code = "error.format.invalid"
        val field = exception.path.lastOrNull()?.fieldName ?: "body"
        val arguments = arrayOf(field, exception.value, exception.targetType.simpleName)
        val message = messageSource.getMessage(code, arguments, requestContext.locale)

        return ErrorModel(code = code, message = message, source = source)
    }

    private fun error(exception: MismatchedInputException): ErrorModel {
        val source = source(exception.path)
        val code = "error.format.incompatible"
        val field = exception.path.lastOrNull()?.fieldName ?: "body"
        val arguments = arrayOf(field, exception.targetType.simpleName)
        val message = messageSource.getMessage(code, arguments, requestContext.locale)

        return ErrorModel(code = code, message = message, source = source)
    }

    private fun error(exception: JsonMappingException): ErrorModel {
        val source = source(exception.path)
        val code = "error.format.invalid"
        val message = exception.originalMessage

        return ErrorModel(code = code, message = message, source = source)
    }

    private fun source(path: List<Reference>): ErrorSource {
        val prefix = if (path.isEmpty()) "/body" else "/body/"
        val pointer = path.map { if (it.index >= 0) it.index else it.fieldName }.joinToString("/", prefix = prefix)

        return ErrorSource(pointer = pointer)
    }
}
