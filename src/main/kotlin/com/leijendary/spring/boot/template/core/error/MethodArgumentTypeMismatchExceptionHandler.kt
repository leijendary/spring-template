package com.leijendary.spring.boot.template.core.error

import com.leijendary.spring.boot.template.core.data.ErrorResponse
import com.leijendary.spring.boot.template.core.util.RequestContext.locale
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.util.ClassUtils
import org.springframework.util.ClassUtils.getShortName
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
@Order(4)
class MethodArgumentTypeMismatchExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(BAD_REQUEST)
    fun catchMethodArgumentTypeMismatch(exception: MethodArgumentTypeMismatchException): ErrorResponse {
        val code = "error.invalid.type"
        val name = exception.name
        val parameter = exception.parameter
        val requiredType = parameter.parameterType.simpleName
        val value = exception.value
        val valueType = ClassUtils.getDescriptiveType(value)?.let { getShortName(it) }
        val arguments = arrayOf(name, value, valueType, requiredType)
        val message = messageSource.getMessage(code, arguments, locale)

        return ErrorResponse.builder()
            .addError(mutableListOf("body", name), code, message)
            .status(BAD_REQUEST)
            .build()
    }
}