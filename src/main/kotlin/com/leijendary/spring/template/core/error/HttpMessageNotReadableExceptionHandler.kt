package com.leijendary.spring.template.core.error

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonMappingException.Reference
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.leijendary.spring.template.core.model.ErrorModel
import com.leijendary.spring.template.core.util.RequestContext.locale
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

private const val PARENT = "body"

@RestControllerAdvice
@Order(2)
class HttpMessageNotReadableExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(BAD_REQUEST)
    fun catchHttpMessageNotReadable(exception: HttpMessageNotReadableException) = errors(exception)

    private fun errors(exception: HttpMessageNotReadableException): List<ErrorModel> {
        val source = listOf(PARENT)
        val code = "error.badRequest"
        var message = exception.message ?: ""
        val error: ErrorModel

        if (message.startsWith("Required request body is missing")) {
            error = ErrorModel(source, code, message.split(":".toRegex()).toTypedArray()[0])

            return listOf(error)
        }

        when (val cause = exception.cause) {
            is InvalidFormatException -> return errors(cause)
            is JsonMappingException -> return errors(cause)
        }

        message = exception.message?.replace("JSON decoding error: ", "") ?: ""
        error = ErrorModel(source, code, message)

        return listOf(error)
    }

    private fun errors(exception: InvalidFormatException): List<ErrorModel> {
        val sources = sources(exception.path)
        val code = "error.body.format.invalid"

        return sources.map {
            val field = it
                // Remove the parent field source
                .subList(1, it.size)
                .joinToString(".")
            val arguments = arrayOf(field, exception.value, exception.targetType.simpleName)
            val message = messageSource.getMessage(code, arguments, locale)

            ErrorModel(it, code, message)
        }
    }

    private fun errors(exception: JsonMappingException): List<ErrorModel> {
        val sources = sources(exception.path)
        val code = "error.body.format.invalid"
        val message = exception.originalMessage

        return sources.map { ErrorModel(it, code, message) }
    }

    private fun sources(path: List<Reference>): List<List<Any>> {
        return path.map {
            val source = mutableListOf<Any>(PARENT, it.fieldName)

            if (it.index >= 0) {
                source.add(1, it.index)
            }

            source
        }
    }
}
