package com.leijendary.error

import com.leijendary.context.RequestContext.locale
import com.leijendary.extension.indexOfReverse
import com.leijendary.extension.lowerCaseFirst
import com.leijendary.extension.snakeCaseToCamelCase
import com.leijendary.model.ErrorModel
import jakarta.servlet.http.HttpServletRequest
import org.postgresql.util.PSQLException
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.http.HttpStatus.*
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI

@RestControllerAdvice
@Order(1)
class DatabaseExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(OptimisticLockingFailureException::class)
    fun handleOptimisticLockingFailure(
        exception: OptimisticLockingFailureException,
        request: HttpServletRequest?
    ): ResponseEntity<Any> {
        val entity = exception.message!!.substringAfterLast(".").lowerCaseFirst()
        val message = messageSource.getMessage(CODE_DATA_VERSION_CONFLICT, null, locale)
        val error = ErrorModel(CODE_DATA_VERSION_CONFLICT, message, "#/data/$entity/version")
        val problemDetail = ProblemDetail.forStatusAndDetail(CONFLICT, "Outdated data version.").apply {
            title = CONFLICT.reasonPhrase
            instance = request?.requestURI?.let(::URI)
            setProperty(PROPERTY_ERRORS, listOf(error))
        }

        return ResponseEntity
            .status(problemDetail.status)
            .body(problemDetail)
    }

    @ExceptionHandler(PSQLException::class)
    fun handlePSQL(exception: PSQLException, request: HttpServletRequest?): ResponseEntity<Any> {
        val errorMessage = exception.serverErrorMessage

        if (errorMessage === null) {
            throw RuntimeException(exception)
        }

        val table = errorMessage.table?.snakeCaseToCamelCase()
        val detail = errorMessage.detail

        if (table === null || detail === null) {
            throw RuntimeException(exception)
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
        val pointer = "#/data/$table/$column"
        val error = ErrorModel(code, message, pointer)
        val problemDetail = ProblemDetail.forStatusAndDetail(status, "Data integrity.").apply {
            title = status.reasonPhrase
            instance = request?.requestURI?.let(::URI)
            setProperty(PROPERTY_ERRORS, listOf(error))
        }

        return ResponseEntity
            .status(status)
            .body(problemDetail)
    }
}
