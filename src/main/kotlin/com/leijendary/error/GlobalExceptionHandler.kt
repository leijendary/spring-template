package com.leijendary.error

import com.leijendary.context.RequestContext.locale
import com.leijendary.context.SpringContext.Companion.isProd
import com.leijendary.extension.logger
import com.leijendary.model.ErrorModel
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI

@RestControllerAdvice
@Order
class GlobalExceptionHandler(private val messageSource: MessageSource) {
    private val log = logger()

    @ExceptionHandler(Exception::class)
    fun handleAll(exception: Exception, request: HttpServletRequest?): ResponseEntity<Any> {
        log.error("Global Exception", exception)

        val message = if (isProd) messageSource.getMessage(CODE_SERVER_ERROR, null, locale) else exception.message
        val error = ErrorModel(CODE_SERVER_ERROR, message, POINTER_SERVER_INTERNAL)
        val problemDetail = ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, "General problem").apply {
            title = INTERNAL_SERVER_ERROR.reasonPhrase
            instance = request?.requestURI?.let(::URI)
            setProperty(PROPERTY_ERRORS, listOf(error))
        }

        return ResponseEntity
            .status(problemDetail.status)
            .body(problemDetail)
    }
}
