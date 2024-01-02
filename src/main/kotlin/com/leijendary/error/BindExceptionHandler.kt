package com.leijendary.error

import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorSource
import com.leijendary.util.RequestContext.locale
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(4)
class BindExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(BindException::class)
    @ResponseStatus(BAD_REQUEST)
    fun catchBind(exception: BindException): List<ErrorModel> {
        val isMethodArgumentNotValid = exception is MethodArgumentNotValidException
        val prefix = if (isMethodArgumentNotValid) "/body/" else ""

        return exception.allErrors.map { field ->
            val objectName = if (field is FieldError) field.field else field.objectName
            val location = objectName.split(".", "[", "]")
                .stream()
                .filter { it.isNotBlank() }
                .map { it.toIntOrNull() ?: it }
                .toArray()
                .joinToString("/", prefix = prefix)
            val source = if (isMethodArgumentNotValid) {
                ErrorSource(pointer = location)
            } else {
                ErrorSource(parameter = location)
            }
            val code = field.defaultMessage ?: ""
            val arguments = field.arguments
            val message = messageSource.getMessage(code, arguments, code, locale)

            ErrorModel(code = code, message = message, source = source)
        }
    }
}
