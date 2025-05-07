package com.leijendary.error

import com.leijendary.context.RequestContext.locale
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.error.exception.ResourceNotUniqueException
import com.leijendary.error.exception.StatusException
import com.leijendary.model.ErrorModel
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI

@RestControllerAdvice
@Order(1)
class BusinessExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(
        exception: ResourceNotFoundException,
        request: HttpServletRequest?
    ): ResponseEntity<in Any>? {
        val arguments = arrayOf(exception.entity)
        val message = messageSource.getMessage(CODE_RESOURCE_NOT_FOUND, arguments, locale)
        val error = ErrorModel(
            id = exception.id.toString(),
            code = CODE_RESOURCE_NOT_FOUND,
            message = message,
            source = exception.source
        )
        val problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, "Data integrity.").apply {
            title = NOT_FOUND.reasonPhrase
            instance = request?.requestURI?.let(::URI)
            setProperty(PROPERTY_ERRORS, listOf(error))
        }

        return ResponseEntity
            .status(problemDetail.status)
            .body(problemDetail)
    }

    @ExceptionHandler(ResourceNotUniqueException::class)
    fun handleResourceNotUnique(
        exception: ResourceNotUniqueException,
        request: HttpServletRequest?
    ): ResponseEntity<in Any>? {
        val source = exception.source
        val field = source.pointer?.split("/")?.last()
        val arguments = arrayOf(field, exception.value)
        val message = messageSource.getMessage(CODE_ALREADY_EXISTS, arguments, locale)
        val error =
            ErrorModel(id = exception.value.toString(), code = CODE_ALREADY_EXISTS, message = message, source = source)
        val problemDetail = ProblemDetail.forStatusAndDetail(CONFLICT, "Data integrity.").apply {
            title = CONFLICT.reasonPhrase
            instance = request?.requestURI?.let(::URI)
            setProperty(PROPERTY_ERRORS, listOf(error))
        }

        return ResponseEntity
            .status(problemDetail.status)
            .body(problemDetail)
    }

    @ExceptionHandler(StatusException::class)
    fun handleStatus(exception: StatusException, request: HttpServletRequest?): ResponseEntity<in Any>? {
        val message = messageSource.getMessage(exception.code, exception.arguments, locale)
        val error = ErrorModel(code = exception.code, message = message, source = exception.source)
        val problemDetail = ProblemDetail.forStatusAndDetail(exception.status, message).apply {
            title = exception.status.reasonPhrase
            instance = request?.requestURI?.let(::URI)
            setProperty(PROPERTY_ERRORS, listOf(error))
        }

        return ResponseEntity
            .status(exception.status)
            .body(problemDetail)
    }
}
