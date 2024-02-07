package com.leijendary.error

import com.leijendary.extension.logger
import com.leijendary.extension.snakeCaseToCamelCase
import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorSource
import com.leijendary.util.locale
import org.postgresql.util.PSQLException
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(3)
class PSQLExceptionHandler(private val messageSource: MessageSource) {
    private val log = logger()

    @ExceptionHandler(PSQLException::class)
    fun catchPSQLException(exception: PSQLException): ResponseEntity<List<ErrorModel>> {
        val errorMessage = exception.serverErrorMessage

        if (errorMessage === null) {
            log.error("Got an unknown database exception", exception)

            val message = messageSource.getMessage(CODE_SERVER_ERROR, emptyArray(), locale)
            val error = ErrorModel(code = CODE_SERVER_ERROR, message = message, source = SOURCE_SERVER_INTERNAL)
            val errors = listOf(error)

            return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(errors)
        }

        val table = errorMessage.table!!.snakeCaseToCamelCase()
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
        val errors = listOf(error)

        return ResponseEntity
            .status(status)
            .body(errors)
    }
}
