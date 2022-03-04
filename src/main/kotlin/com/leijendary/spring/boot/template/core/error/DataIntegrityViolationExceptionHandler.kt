package com.leijendary.spring.boot.template.core.error

import com.leijendary.spring.boot.template.core.data.ErrorData
import com.leijendary.spring.boot.template.core.extension.snakeCaseToCamelCase
import com.leijendary.spring.boot.template.core.util.RequestContext.locale
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
    fun catchDataIntegrityViolation(exception: DataIntegrityViolationException): ResponseEntity<List<ErrorData>> {
        return getResponse(exception)
    }

    private fun getResponse(exception: DataIntegrityViolationException): ResponseEntity<List<ErrorData>> {
        val cause: Throwable? = exception.cause

        if (cause is ConstraintViolationException) {
            return constraintViolationException(cause)
        }

        val code = "error.dataIntegrity"
        val arguments: Array<String> = arrayOf(exception.message ?: "")
        val message = messageSource.getMessage(code, arguments, locale)
        val errorData = ErrorData(listOf("data", "entity"), code, message)

        return ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(listOf(errorData))
    }

    private fun constraintViolationException(exception: ConstraintViolationException): ResponseEntity<List<ErrorData>> {
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
        val errorData = ErrorData(listOf("data", table, field), code, message)

        return ResponseEntity
            .status(CONFLICT)
            .body(listOf(errorData))
    }
}