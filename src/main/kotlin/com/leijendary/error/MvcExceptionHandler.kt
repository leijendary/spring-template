package com.leijendary.error

import com.leijendary.context.RequestContext.currentRequest
import com.leijendary.context.RequestContext.isApi
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.net.URI

@ControllerAdvice
@Order(2)
class MvcExceptionHandler {
    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFound(exception: NoResourceFoundException): Any {
        if (!isApi) {
            return "404"
        }

        val problemDetail = exception.body.apply {
            title = HttpStatus.NOT_FOUND.reasonPhrase
            instance = currentRequest?.requestURI?.let(::URI)
        }

        return ResponseEntity
            .status(exception.statusCode)
            .body(problemDetail)
    }
}
