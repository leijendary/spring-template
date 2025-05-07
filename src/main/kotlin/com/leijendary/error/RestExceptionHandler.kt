package com.leijendary.error

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonMappingException.Reference
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.leijendary.context.RequestContext.locale
import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
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
                val source = getSource(cause.path)
                val field = cause.path.lastOrNull()?.fieldName ?: "body"
                val arguments = arrayOf(field, cause.value, cause.targetType.simpleName)
                val message = messageSource?.getMessage(CODE_FORMAT_INVALID, arguments, locale)
                val error = ErrorModel(code = CODE_FORMAT_INVALID, message = message, source = source)

                return withErrors(response, listOf(error))
            }

            is MismatchedInputException -> {
                val source = getSource(cause.path)
                val field = cause.path.lastOrNull()?.fieldName ?: "body"
                val arguments = cause.targetType?.let { arrayOf<Any>(field, it.simpleName) }
                val code = if (cause.targetType !== null) CODE_FORMAT_INCOMPATIBLE else CODE_REQUIRED
                val message = messageSource?.getMessage(code, arguments, locale)
                val error = ErrorModel(code = code, message = message, source = source)

                return withErrors(response, listOf(error))
            }

            is JsonMappingException -> {
                val source = getSource(cause.path)
                val (code, message) = if (cause.cause is NullPointerException) {
                    CODE_REQUIRED to messageSource?.getMessage(CODE_REQUIRED, null, locale)
                } else {
                    CODE_FORMAT_INVALID to cause.originalMessage
                }
                val error = ErrorModel(code = code, message = message, source = source)

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
        val errors = exception.allErrors.map(::mapToErrorModel)

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
            val source = ErrorSource(parameter = result.methodParameter.parameterName ?: "")

            result.resolvableErrors.map {
                if (it is ObjectError) {
                    mapToErrorModel(it)
                } else {
                    val code = it.defaultMessage ?: CODE_BAD_REQUEST
                    val message = messageSource?.getMessage(code, it.arguments, locale)

                    ErrorModel(code = code, message = message, source = source)
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
        val source = ErrorSource(pointer = "/body/${exception.requestPartName}")
        val error = ErrorModel(code = CODE_BAD_REQUEST, message = message, source = source)

        return withErrors(response, listOf(error))
    }

    private fun getSource(path: List<Reference>): ErrorSource {
        val prefix = if (path.isEmpty()) "/body" else "/body/"
        val pointer = path.map { if (it.index >= 0) it.index else it.fieldName }.joinToString("/", prefix = prefix)

        return ErrorSource(pointer = pointer)
    }

    private fun mapToErrorModel(error: ObjectError): ErrorModel {
        val (prefix, field, isBindingFailure) = if (error is FieldError) {
            val prefix = if (!error.isBindingFailure) "/body/" else ""

            Triple(prefix, error.field, error.isBindingFailure)
        } else {
            Triple("", error.objectName, true)
        }
        val location = field.split(".", "[", "]")
            .stream()
            .filter { it.isNotBlank() }
            .map { it.toIntOrNull() ?: it }
            .toArray()
            .joinToString("/", prefix = prefix)
        val source = if (isBindingFailure) ErrorSource(parameter = location) else ErrorSource(pointer = location)
        val code = if (!error.shouldRenderDefaultMessage()) {
            error.defaultMessage ?: CODE_BINDING_INVALID_VALUE
        } else {
            CODE_BINDING_INVALID_VALUE
        }
        val message = messageSource?.getMessage(code, error.arguments, code, locale)

        return ErrorModel(code = code, message = message, source = source)
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
