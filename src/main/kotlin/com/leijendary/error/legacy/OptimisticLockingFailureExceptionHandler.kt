package com.leijendary.error.legacy

import com.leijendary.context.RequestContext.locale
import com.leijendary.extension.lowerCaseFirst
import com.leijendary.model.ErrorModel
import com.leijendary.model.ErrorSource
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

// @RestControllerAdvice
@Order(3)
class OptimisticLockingFailureExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(OptimisticLockingFailureException::class)
    @ResponseStatus(CONFLICT)
    fun catchOptimisticLockingFailure(exception: OptimisticLockingFailureException): List<ErrorModel> {
        val entity = exception.message!!.substringAfterLast(".").lowerCaseFirst()
        val code = "error.data.version.conflict"
        val message = messageSource.getMessage(code, null, locale)
        val source = ErrorSource(pointer = "/data/$entity/version")
        val error = ErrorModel(code = code, message = message, source = source)

        return listOf(error)
    }
}
