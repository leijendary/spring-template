package com.leijendary.error.legacy

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonMappingException.Reference
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.leijendary.context.RequestContext.isApi
import com.leijendary.context.RequestContext.locale
import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorModelsResponse
import com.leijendary.model.ErrorSource
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

private const val MESSAGE_BODY_MISSING = "Required request body is missing"
private const val MESSAGE_DECODING_ERROR = "JSON decoding error: "
private val SOURCE = ErrorSource(pointer = "/body")

// @ControllerAdvice
@Order(2)
class HttpMessageNotReadableExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(BAD_REQUEST)
    @ErrorModelsResponse(status = "400", description = "Request message is not readable.")
    fun catchHttpMessageNotReadable(exception: HttpMessageNotReadableException): Any {
        if (!isApi) {
            return "400"
        }

        val code = "error.badRequest"
        var message = exception.message?.split(":".toRegex())?.toTypedArray()?.get(0) ?: ""
        val error: ErrorModel

        if (message.startsWith(MESSAGE_BODY_MISSING)) {
            error = ErrorModel(code = code, message = message, source = SOURCE)

            return listOf(error)
        }

        error = when (val cause = exception.cause) {
            is InvalidFormatException -> error(cause)
            is MismatchedInputException -> error(cause)
            is JsonMappingException -> error(cause)
            else -> {
                message = exception.message?.replace(MESSAGE_DECODING_ERROR, "") ?: ""

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
        val message = messageSource.getMessage(code, arguments, locale)

        return ErrorModel(code = code, message = message, source = source)
    }

    private fun error(exception: MismatchedInputException): ErrorModel {
        val source = source(exception.path)
        val field = exception.path.lastOrNull()?.fieldName ?: "body"
        val (code, message) = if (exception.targetType !== null) {
            val code = "error.format.incompatible"
            val arguments = arrayOf(field, exception.targetType.simpleName)
            code to messageSource.getMessage(code, arguments, locale)
        } else {
            val code = "validation.required"
            code to messageSource.getMessage(code, null, locale)
        }

        return ErrorModel(code = code, message = message, source = source)
    }

    private fun error(exception: JsonMappingException): ErrorModel {
        val source = source(exception.path)
        val (code, message) = if (exception.cause is NullPointerException) {
            val code = "validation.required"
            code to messageSource.getMessage(code, null, locale)
        } else {
            "error.format.invalid" to exception.originalMessage
        }

        return ErrorModel(code = code, message = message, source = source)
    }

    private fun source(path: List<Reference>): ErrorSource {
        val prefix = if (path.isEmpty()) "/body" else "/body/"
        val pointer = path.map { if (it.index >= 0) it.index else it.fieldName }.joinToString("/", prefix = prefix)

        return ErrorSource(pointer = pointer)
    }
}
