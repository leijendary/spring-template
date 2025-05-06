package com.leijendary.error

import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    override fun createProblemDetail(
        ex: Exception,
        status: HttpStatusCode,
        defaultDetail: String,
        detailMessageCode: String?,
        detailMessageArguments: Array<out Any?>?,
        request: WebRequest
    ): ProblemDetail {
        return super.createProblemDetail(ex, status, defaultDetail, detailMessageCode, detailMessageArguments, request)
    }
}
