package com.leijendary.spring.core.error

import com.leijendary.spring.core.exception.VersionConflictException
import com.leijendary.spring.core.model.ErrorModel
import com.leijendary.spring.core.model.ErrorSource
import com.leijendary.spring.core.util.RequestContext.locale
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@Component
@Order(4)
class VersionConflictExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(VersionConflictException::class)
    @ResponseStatus(CONFLICT)
    fun catchVersionConflict(exception: VersionConflictException): List<ErrorModel> {
        val code = "error.data.version.conflict"
        val message = messageSource.getMessage(code, emptyArray(), locale)
        val source = ErrorSource(pointer = "/data/${exception.entity}/version")
        val error = ErrorModel(id = exception.id.toString(), code = code, message = message, source = source)

        return listOf(error)
    }
}
