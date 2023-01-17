package com.leijendary.spring.template.core.error

import com.leijendary.spring.template.core.data.ErrorResponse
import com.leijendary.spring.template.core.extension.snakeCaseToCamelCase
import com.leijendary.spring.template.core.util.RequestContext.locale
import org.apache.commons.lang3.StringUtils.substringBetween
import org.hibernate.exception.ConstraintViolationException
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
        val errorMessage = exception.sqlException.nextException.message!!
        val sql = exception.sql
        val table = sql.let {
            substringBetween(it, "insert into ", " (")
                ?: substringBetween(it, "update ", " set ")
        }.snakeCaseToCamelCase(true)
        val field = errorMessage
            .substringAfter("Key (")
            .substringBefore(")=")
            .substringAfter("(")
            .substringBefore("::")
            .snakeCaseToCamelCase()
        val value = errorMessage
            .substringAfter("=(")
            .substringBefore(") ")
        val code = "validation.alreadyExists"
        val arguments = arrayOf(field, value)
        val message = messageSource.getMessage(code, arguments, locale)

        return ErrorResponse.builder()
            .addError(mutableListOf("data", table, field), code, message)
            .status(CONFLICT)
            .build()
    }
}