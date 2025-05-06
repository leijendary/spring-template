package com.leijendary.error.legacy

import com.leijendary.error.exception.ErrorModelException
import com.leijendary.model.ErrorModel
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler

// @RestControllerAdvice
@Order(3)
class ErrorModelExceptionHandler {
    @ExceptionHandler(ErrorModelException::class)
    fun catchErrorModel(exception: ErrorModelException): ResponseEntity<List<ErrorModel>> {
        return ResponseEntity
            .status(exception.status)
            .body(exception.errors)
    }
}
