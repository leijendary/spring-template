package com.leijendary.spring.core.error

import com.leijendary.spring.core.model.ErrorModel
import com.leijendary.spring.core.model.ErrorSource
import com.leijendary.spring.core.util.RequestContext.locale
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
    fun catchMethodArgumentTypeMismatch(exception: MethodArgumentTypeMismatchException): List<ErrorModel> {
        val code = "error.invalid.type"
        val name = exception.name
        val parameter = exception.parameter
        val requiredType = parameter.parameterType.simpleName
        val value = exception.value
        val valueType = ClassUtils.getDescriptiveType(value)?.let(::getShortName)
        val arguments = arrayOf(name, value, valueType, requiredType)
        val message = messageSource.getMessage(code, arguments, locale)
        val source = ErrorSource(pointer = "/body/$name")
        val error = ErrorModel(code = code, message = message, source = source)

        return listOf(error)
    }
}
