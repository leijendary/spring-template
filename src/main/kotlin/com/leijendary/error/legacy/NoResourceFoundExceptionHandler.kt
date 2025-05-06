package com.leijendary.error.legacy

import com.leijendary.context.RequestContext.isApi
import com.leijendary.context.RequestContext.isFragment
import com.leijendary.context.RequestContext.locale
import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorModelsResponse
import com.leijendary.model.ErrorSource
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.resource.NoResourceFoundException

private val SOURCE = ErrorSource(pointer = "/path")

// @ControllerAdvice
@Order(1)
class NoResourceFoundExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(NoResourceFoundException::class)
    @ResponseStatus(NOT_FOUND)
    @ErrorModelsResponse(status = "404", description = "Mapping not found.")
    fun catchNoResourceFound(exception: NoResourceFoundException, model: Model): Any {
        if (!isApi || isFragment) {
            model.addAttribute("message", "hello there")
            return "404"
        }

        val code = "error.mapping.notFound"
        val arguments = arrayOf(exception.httpMethod.name(), "/${exception.resourcePath}")
        val message = messageSource.getMessage(code, arguments, locale)
        val error = ErrorModel(code = code, message = message, source = SOURCE)

        return ResponseEntity.status(NOT_FOUND).body(listOf(error))
    }
}
