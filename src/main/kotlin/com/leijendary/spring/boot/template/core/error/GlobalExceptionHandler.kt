package com.leijendary.spring.boot.template.core.error

import com.leijendary.spring.boot.template.core.data.ErrorData
import com.leijendary.spring.boot.template.core.extension.logger
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order
class GlobalExceptionHandler {
    val log = logger()

    @ExceptionHandler(Exception::class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    fun catchException(exception: Exception): List<ErrorData> {
        log.error("Global Exception", exception)

        val code = "error.generic"
        val errorData = ErrorData(listOf("server", "internal"), code, exception.message)

        return listOf(errorData)
    }
}