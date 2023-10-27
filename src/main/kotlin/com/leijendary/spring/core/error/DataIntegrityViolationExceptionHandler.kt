package com.leijendary.spring.core.error

import com.leijendary.spring.core.extension.logger
import com.leijendary.spring.core.extension.snakeCaseToCamelCase
import com.leijendary.spring.core.model.ErrorModel
import com.leijendary.spring.core.model.ErrorSource
import com.leijendary.spring.core.util.RequestContext.locale
import org.postgresql.util.PSQLException
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(3)
class DataIntegrityViolationExceptionHandler(private val messageSource: MessageSource) {
    private val log = logger()

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun catchDataIntegrityViolation(exception: DataIntegrityViolationException): ResponseEntity<List<ErrorModel>> {
        val (status, errors) = errorPair(exception)

        return ResponseEntity
            .status(status)
            .body(errors)
    }

    private fun errorPair(exception: DataIntegrityViolationException): Pair<HttpStatus, List<ErrorModel>> {
        val cause = exception.cause

        if (cause is PSQLException) {
            return sqlException(cause)
        }

        log.error("Data Integrity Error", exception)

        val code = "error.data.integrity"
        val arguments = arrayOf(exception.message ?: "")
        val message = messageSource.getMessage(code, arguments, locale)
        val source = ErrorSource(pointer = "/data/entity")
        val error = ErrorModel(code = code, message = message, source = source)

        return INTERNAL_SERVER_ERROR to listOf(error)
    }

    private fun sqlException(exception: PSQLException): Pair<HttpStatus, List<ErrorModel>> {
        val errorMessage = exception.serverErrorMessage!!
        val table = errorMessage.table!!.snakeCaseToCamelCase(true)
        val detail = errorMessage.detail!!
        val column = errorMessage.column ?: detail
            .substringAfter("Key (")
            .substringBefore("))=")
            .substringBefore(")=")
            .substringAfter("(")
            .substringBefore("::")
        val field = column.snakeCaseToCamelCase()
        val value = detail.substringAfter("=(").substringBefore(") ")
        val (code, status) = when (exception.sqlState) {
            "23505" -> "validation.alreadyExists" to CONFLICT
            "23503" -> "error.resource.notFound" to NOT_FOUND
            else -> "error.data.integrity" to BAD_REQUEST
        }
        val arguments = arrayOf(field, value)
        val message = messageSource.getMessage(code, arguments, locale)
        val source = ErrorSource(pointer = "/data/$table/$field")
        val error = ErrorModel(code = code, message = message, source = source)

        return status to listOf(error)
    }
}
