package com.leijendary.spring.template.core.error

import com.leijendary.spring.template.core.model.ErrorModel
import com.leijendary.spring.template.core.util.RequestContext.locale
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(5)
class BindExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(BindException::class)
    @ResponseStatus(BAD_REQUEST)
    fun catchBind(exception: BindException): List<ErrorModel> {
        return exception.allErrors.map { field: ObjectError ->
            val objectName: String = if (field is FieldError) field.field else field.objectName
            val source: List<String> = listOf("param") + objectName.split(".").map {
                it.replace("[]", "")
            }
            val code: String = field.defaultMessage ?: ""
            val arguments: Array<Any>? = field.arguments
            val message = code.let { messageSource.getMessage(it, arguments, code, locale) }

            ErrorModel(source, code, message)
        }
    }
}
