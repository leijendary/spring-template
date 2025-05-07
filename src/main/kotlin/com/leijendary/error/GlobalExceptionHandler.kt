package com.leijendary.error

import com.leijendary.context.RequestContext.isApi
import com.leijendary.context.RequestContext.locale
import com.leijendary.context.SpringContext.Companion.isProd
import com.leijendary.extension.logger
import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorModelsResponse
import com.leijendary.model.ErrorSource
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

const val CODE_SERVER_ERROR = "error.serverError"
val SOURCE_SERVER_INTERNAL = ErrorSource(pointer = "/server/internal")

// @ControllerAdvice
@Order
class GlobalExceptionHandler(private val messageSource: MessageSource) {
    private val log = logger()

    @ExceptionHandler(Exception::class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ErrorModelsResponse(status = "500", description = "Generic server error.")
    fun catchException(exception: Exception): Any {
        log.error("Global Exception", exception)

        if (!isApi) {
            return "error"
        }

        val message = if (isProd) messageSource.getMessage(CODE_SERVER_ERROR, null, locale) else exception.message
        val error = ErrorModel(code = CODE_SERVER_ERROR, message = message, source = SOURCE_SERVER_INTERNAL)

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(listOf(error))
    }
}
