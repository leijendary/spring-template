package com.leijendary.spring.template.core.error

import com.leijendary.spring.template.core.data.ErrorResponse
import com.leijendary.spring.template.core.extension.snakeCaseToCamelCase
import com.leijendary.spring.template.core.util.RequestContext.locale
import jakarta.validation.ConstraintViolationException
import org.apache.commons.lang3.StringUtils.substringBetween
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(3)
class DataIntegrityViolationExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun catchDataIntegrityViolation(exception: DataIntegrityViolationException): ResponseEntity<ErrorResponse> {
        val errorResponse: ErrorResponse = getResponse(exception)
        val status: Any? = errorResponse.meta["status"]

        return ResponseEntity
            .status(status as Int)
            .body(errorResponse)
    }

    private fun getResponse(exception: DataIntegrityViolationException): ErrorResponse {
        val cause: Throwable? = exception.cause

        if (cause is ConstraintViolationException) {
            return constraintViolationException(cause)
        }

        val code = "error.data.integrity"
        val arguments: Array<String> = arrayOf(exception.message ?: "")
        val message = messageSource.getMessage(code, arguments, locale)

        return ErrorResponse.builder()
            .addError(mutableListOf("data", "entity"), code, message)
            .status(INTERNAL_SERVER_ERROR)
            .build()
    }

    private fun constraintViolationException(exception: ConstraintViolationException): ErrorResponse {
        val builder = ErrorResponse.builder()

        exception.constraintViolations.forEach {
            val code = "validation.alreadyExists"
            val arguments = arrayOf(it.propertyPath, it.message)
            val message = messageSource.getMessage(code, arguments, locale)

            builder.addError(mutableListOf("data", it.propertyPath, it.propertyPath), code, message)
        }

        return ErrorResponse.builder()
            .status(CONFLICT)
            .build()
    }
}