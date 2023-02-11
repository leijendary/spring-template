package com.leijendary.spring.template.core.error

import com.leijendary.spring.template.core.extension.snakeCaseToCamelCase
import com.leijendary.spring.template.core.model.ErrorModel
import com.leijendary.spring.template.core.util.RequestContext.locale
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@Component
@Order(4)
class OptimisticLockingFailureExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(OptimisticLockingFailureException::class)
    @ResponseStatus(BAD_REQUEST)
    fun catchOptimisticLockingFailure(exception: OptimisticLockingFailureException): List<ErrorModel> {
        val exceptionMessage = exception.message!!
        val table = exceptionMessage
            .substringAfter("table [")
            .substringBefore("].")
            .snakeCaseToCamelCase(true)
        val source = listOf("data") + table + "version"
        val code = "error.data.version.conflict"
        val message = messageSource.getMessage(code, emptyArray(), locale)
        val error = ErrorModel(source, code, message)

        return listOf(error)
    }
}
