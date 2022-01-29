package com.leijendary.spring.boot.template.core.error

import com.leijendary.spring.boot.template.core.data.ErrorResponse
import com.leijendary.spring.boot.template.core.data.ErrorResponse.ErrorResponseBuilder
import com.leijendary.spring.boot.template.core.util.RequestContext.locale
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(4)
class MethodArgumentNotValidExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(BAD_REQUEST)
    fun catchMethodArgumentNotValid(exception: MethodArgumentNotValidException): ErrorResponse {
        val response: ErrorResponseBuilder = ErrorResponse.builder().status(BAD_REQUEST)

        exception.allErrors.forEach { field: ObjectError ->
            val objectName: String = if (field is FieldError) field.field else field.objectName
            val source: List<String> = objectName.split("\\.").map {
                it.replace("[]", "")
            }
            val code: String = field.defaultMessage ?: ""
            val arguments: Array<Any>? = field.arguments
            val message = code.let { messageSource.getMessage(it, arguments, code, locale) }

            response.addError(source, code, message)
        }

        return response.build()
    }
}