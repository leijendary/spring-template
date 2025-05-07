package com.leijendary.error

import com.leijendary.context.RequestContext.locale
import com.leijendary.extension.indexOfReverse
import com.leijendary.extension.logger
import com.leijendary.extension.snakeCaseToCamelCase
import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorSource
import org.postgresql.util.PSQLException
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler

private const val CODE_DATA_INTEGRITY = "error.data.integrity"
private const val CODE_DATA_REFERENCED = "error.data.referenced"
private const val CODE_RESOURCE_NOT_FOUND = "error.resource.notFound"
private const val CODE_ALREADY_EXISTS = "validation.alreadyExists"
private const val DETAIL_STILL_REFERENCED = "is still referenced"

// @RestControllerAdvice
@Order(3)
class PSQLExceptionHandler(private val messageSource: MessageSource) {
    private val log = logger()

    @ExceptionHandler(PSQLException::class)
    fun catchPSQLException(exception: PSQLException): ResponseEntity<List<ErrorModel>> {
        val errorMessage = exception.serverErrorMessage

        if (errorMessage === null) {
            return internalServerError(exception)
        }

        val table = errorMessage.table?.snakeCaseToCamelCase()
        val detail = errorMessage.detail

        if (table === null || detail === null) {
            return internalServerError(exception)
        }

        val key = detail.substringAfter("Key (").substringBefore(")=")
        val column = errorMessage.column?.snakeCaseToCamelCase() ?: key
            .substringAfter('(')
            .substringBefore(',')
            .substringBefore("::")
            .snakeCaseToCamelCase()
        var value = detail.substringAfter("=(").substringBeforeLast(") ")
        val commas = key.count { it == ',' }

        if (commas > 0) {
            val i = value.indexOfReverse(',', commas)
            value = value.substring(0, i)
        }

        val (code, status) = when (exception.sqlState) {
            "23503" -> if (detail.contains(DETAIL_STILL_REFERENCED)) {
                CODE_DATA_REFERENCED to CONFLICT
            } else {
                CODE_RESOURCE_NOT_FOUND to NOT_FOUND
            }

            "23505" -> CODE_ALREADY_EXISTS to CONFLICT

            else -> CODE_DATA_INTEGRITY to BAD_REQUEST
        }
        val arguments = arrayOf(column, value)
        val message = messageSource.getMessage(code, arguments, locale)
        val source = ErrorSource(pointer = "/data/$table/$column")
        val error = ErrorModel(code = code, message = message, source = source)
        val errors = listOf(error)

        return ResponseEntity
            .status(status)
            .body(errors)
    }

    private fun internalServerError(exception: PSQLException): ResponseEntity<List<ErrorModel>> {
        log.error("Got an unknown database exception", exception)

        val message = messageSource.getMessage(CODE_SERVER_ERROR, null, locale)
        val error = ErrorModel(code = CODE_SERVER_ERROR, message = message, source = SOURCE_SERVER_INTERNAL)
        val errors = listOf(error)

        return ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(errors)
    }
}
