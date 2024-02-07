package com.leijendary.error

import com.leijendary.error.exception.VersionConflictException
import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorSource
import com.leijendary.util.locale
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(3)
class VersionConflictExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(VersionConflictException::class)
    @ResponseStatus(CONFLICT)
    fun catchVersionConflict(exception: VersionConflictException): List<ErrorModel> {
        val code = "error.data.version.conflict"
        val message = messageSource.getMessage(code, emptyArray(), locale)
        val source = ErrorSource(
            pointer = "/data/${exception.entity}/version",
            meta = mapOf("version" to exception.version)
        )
        val error = ErrorModel(id = exception.id.toString(), code = code, message = message, source = source)

        return listOf(error)
    }
}
