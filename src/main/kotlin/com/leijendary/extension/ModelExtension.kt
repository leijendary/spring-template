package com.leijendary.extension

import com.leijendary.context.RequestContext.currentRequest
import com.leijendary.context.SpringContext.Companion.messageSource
import com.leijendary.error.PROPERTY_ERROR_MAP
import com.leijendary.error.PROPERTY_PROBLEM_DETAIL
import com.leijendary.model.toMap
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ProblemDetail
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import java.net.URI

fun Model.withProblemDetail(bindingResult: BindingResult): Model {
    val errorMap = bindingResult.allErrors.toErrorModels(messageSource).toMap()
    val problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Invalid request content.").apply {
        title = BAD_REQUEST.reasonPhrase
        instance = currentRequest?.requestURI?.let(::URI)
        setProperty(PROPERTY_ERROR_MAP, errorMap)
    }

    addAttribute(PROPERTY_PROBLEM_DETAIL, problemDetail)

    return this
}
