package com.leijendary.spring.template.core.error

import com.leijendary.spring.template.core.model.ErrorModel
import com.leijendary.spring.template.core.util.RequestContext.locale
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(4)
class MethodArgumentNotValidExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(BAD_REQUEST)
    fun catchMethodArgumentNotValid(exception: MethodArgumentNotValidException): List<ErrorModel> {
        return exception.allErrors.map { field ->
            val objectName = if (field is FieldError) field.field else field.objectName
            val source = listOf("body") + objectName.split(".").map {
                it.replace("[]", "")
            }
            val code = field.defaultMessage ?: ""
            val arguments = field.arguments
            val message = code.let { messageSource.getMessage(it, arguments, code, locale) }

            ErrorModel(source, code, message)
        }
    }
}
