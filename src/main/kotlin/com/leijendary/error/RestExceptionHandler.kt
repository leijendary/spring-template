package com.leijendary.error

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonMappingException.Reference
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.leijendary.context.RequestContext.locale
import com.leijendary.extension.toErrorModel
import com.leijendary.extension.toErrorModels
import com.leijendary.model.ErrorModel
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.ObjectError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.net.URI

@RestControllerAdvice(annotations = [RestController::class])
@Order(1)
class RestExceptionHandler : ResponseEntityExceptionHandler() {
    override fun handleHttpRequestMethodNotSupported(
        exception: HttpRequestMethodNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<in Any>? {
        // Just to ignore logs.
        return super.handleExceptionInternal(exception, null, headers, status, request)
    }

    override fun handleHttpMessageNotReadable(
        exception: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<in Any>? {
        val response = super.handleHttpMessageNotReadable(exception, headers, status, request)

        when (val cause = exception.cause) {
            is InvalidFormatException -> {
                val pointer = getPointer(cause.path)
                val field = cause.path.lastOrNull()?.fieldName ?: "body"
                val arguments = arrayOf(field, cause.value, cause.targetType.simpleName)
                val message = messageSource?.getMessage(CODE_FORMAT_INVALID, arguments, locale)
                val error = ErrorModel(CODE_FORMAT_INVALID, message, pointer)

                return withErrors(response, listOf(error))
            }

            is MismatchedInputException -> {
                val pointer = getPointer(cause.path)
                val field = cause.path.lastOrNull()?.fieldName ?: "body"
                val arguments = cause.targetType?.let { arrayOf<Any>(field, it.simpleName) }
                val code = if (cause.targetType !== null) CODE_FORMAT_INCOMPATIBLE else CODE_REQUIRED
                val message = messageSource?.getMessage(code, arguments, locale)
                val error = ErrorModel(code, message, pointer)

                return withErrors(response, listOf(error))
            }

            is JsonMappingException -> {
                val pointer = getPointer(cause.path)
                val (code, message) = if (cause.cause is NullPointerException) {
                    CODE_REQUIRED to messageSource?.getMessage(CODE_REQUIRED, null, locale)
                } else {
                    CODE_FORMAT_INVALID to cause.originalMessage
                }
                val error = ErrorModel(code, message, pointer)

                return withErrors(response, listOf(error))
            }
        }

        return response
    }

    override fun handleMethodArgumentNotValid(
        exception: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<in Any>? {
        val response = super.handleMethodArgumentNotValid(exception, headers, status, request)
        val errors = exception.allErrors.toErrorModels(messageSource)

        return withErrors(response, errors)
    }

    override fun handleHandlerMethodValidationException(
        exception: HandlerMethodValidationException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<in Any>? {
        val response = super.handleHandlerMethodValidationException(exception, headers, status, request)
        val errors = exception.parameterValidationResults.flatMap { result ->
            var pointer = "#/parameter"

            if (result.methodParameter.parameterName !== null) {
                pointer += "/${result.methodParameter.parameterName}"
            }

            result.resolvableErrors.map {
                if (it is ObjectError) {
                    it.toErrorModel(messageSource)
                } else {
                    val code = it.defaultMessage ?: CODE_BAD_REQUEST
                    val message = messageSource?.getMessage(code, it.arguments, locale)

                    ErrorModel(code, message, pointer)
                }
            }
        }

        return withErrors(response, errors)
    }

    override fun handleMissingServletRequestPart(
        exception: MissingServletRequestPartException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<in Any>? {
        val response = super.handleMissingServletRequestPart(exception, headers, status, request)
        val message = messageSource?.getMessage(CODE_BAD_REQUEST, null, locale)
        val error = ErrorModel(CODE_BAD_REQUEST, message, "#/body/${exception.requestPartName}")

        return withErrors(response, listOf(error))
    }

    private fun getPointer(path: List<Reference>): String {
        var prefix = "#/body"

        if (path.isNotEmpty()) {
            prefix += "/"
        }

        return path.map { if (it.index >= 0) it.index else it.fieldName }.joinToString("/", prefix = prefix)
    }

    private fun withErrors(
        response: ResponseEntity<in Any>?,
        errors: List<ErrorModel>,
        type: URI = URI("about:blank")
    ): ResponseEntity<in Any>? {
        val body = response?.body as? ProblemDetail ?: return null
        body.type = type
        body.setProperty(PROPERTY_ERRORS, errors)

        return ResponseEntity(body, response.headers, response.statusCode)
    }
}
