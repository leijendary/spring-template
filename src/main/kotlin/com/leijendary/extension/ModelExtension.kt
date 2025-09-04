package com.leijendary.extension

import com.leijendary.context.RequestContext.currentRequest
import com.leijendary.context.SpringContext.Companion.messageSource
import com.leijendary.error.PROPERTY_ERRORS
import com.leijendary.error.PROPERTY_PROBLEM_DETAIL
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ProblemDetail
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import java.net.URI

fun Model.withProblemDetail(bindingResult: BindingResult): Model {
    val errors = bindingResult.allErrors.toErrorModels(messageSource)
    val problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Invalid request content.").apply {
        title = BAD_REQUEST.reasonPhrase
        instance = currentRequest?.requestURI?.let(::URI)
        setProperty(PROPERTY_ERRORS, errors)
    }

    addAttribute(PROPERTY_PROBLEM_DETAIL, problemDetail)

    return this
}
