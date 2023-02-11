package com.leijendary.spring.template.core.error

import com.leijendary.spring.template.core.extension.snakeCaseToCamelCase
import com.leijendary.spring.template.core.model.ErrorModel
import com.leijendary.spring.template.core.util.RequestContext.locale
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
    fun catchDataIntegrityViolation(exception: DataIntegrityViolationException): ResponseEntity<List<ErrorModel>> {
        val (errors, status) = errorPair(exception)

        return ResponseEntity
            .status(status)
            .body(errors)
    }

    private fun errorPair(exception: DataIntegrityViolationException): Pair<List<ErrorModel>, HttpStatus> {
        val cause: Throwable? = exception.cause

        if (cause is ConstraintViolationException) {
            return constraint(cause)
        }

        val code = "error.data.integrity"
        val arguments: Array<String> = arrayOf(exception.message ?: "")
        val message = messageSource.getMessage(code, arguments, locale)
        val error = ErrorModel(mutableListOf("data", "entity"), code, message)

        return listOf(error) to INTERNAL_SERVER_ERROR
    }

    private fun constraint(exception: ConstraintViolationException): Pair<List<ErrorModel>, HttpStatus> {
        val errorMessage = exception.sqlException.nextException.message!!
        val sql = exception.sql
        val table = sql
            .let {
                substringBetween(it, "insert into ", " (")
                    ?: substringBetween(it, "update ", " set ")
            }
            .snakeCaseToCamelCase(true)
        val field = errorMessage
            .substringAfter("Key (")
            .substringBefore(")=")
            .substringAfter("(")
            .substringBefore("::")
            .snakeCaseToCamelCase()
        val value = errorMessage.substringAfter("=(").substringBefore(") ")
        val code = "validation.alreadyExists"
        val arguments = arrayOf(field, value)
        val message = messageSource.getMessage(code, arguments, locale)
        val error = ErrorModel(mutableListOf("data", table, field), code, message)

        return listOf(error) to CONFLICT
    }
}
