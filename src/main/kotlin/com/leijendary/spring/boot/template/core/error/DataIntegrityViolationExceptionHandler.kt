package com.leijendary.spring.boot.template.core.error

import com.leijendary.spring.boot.template.core.data.ErrorResponse
import com.leijendary.spring.boot.template.core.util.RequestContext.locale
import com.leijendary.spring.boot.template.core.util.snakeCaseToCamelCase
import org.apache.commons.lang3.StringUtils.substringBetween
import org.hibernate.exception.ConstraintViolationException
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
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

        val code = "error.dataIntegrity"
        val arguments: Array<String> = arrayOf(exception.message ?: "")
        val message = messageSource.getMessage(code, arguments, locale)

        return ErrorResponse.builder()
            .addError(mutableListOf("data", "entity"), code, message)
            .status(INTERNAL_SERVER_ERROR)
            .build()
    }

    private fun constraintViolationException(exception: ConstraintViolationException): ErrorResponse {
        val errorMessage = exception.sqlException.nextException.message
        val sql: String = exception.sql
        val table: String = sql.let {
            substringBetween(it, "insert into ", " (") ?: substringBetween(it, "update ", " set ")
        }.snakeCaseToCamelCase(true)
        val field: String = substringBetween(errorMessage, "Key (", ")=").snakeCaseToCamelCase()
        val value = substringBetween(errorMessage, "=(", ") ")
        val code = "validation.alreadyExists"
        val arguments = arrayOf(field, value)
        val message = messageSource.getMessage(code, arguments, locale)

        return ErrorResponse.builder()
            .addError(mutableListOf("data", table, field), code, message)
            .status(CONFLICT)
            .build()
    }
}