package com.leijendary.error

import com.leijendary.context.RequestContext.isFragment
import com.leijendary.extension.toErrorModels
import com.leijendary.model.toMap
import org.springframework.context.MessageSource
import org.springframework.core.annotation.Order
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.resource.NoResourceFoundException

@ControllerAdvice
@Order(2)
class MvcExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFound(): String {
        return "404"
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleHandlerMethodValidation(
        exception: MethodArgumentNotValidException,
        handlerMethod: HandlerMethod
    ): ModelAndView {
        val errors = exception.bindingResult.allErrors.toErrorModels(messageSource).toMap()
        val problemDetail = exception.body.apply { setProperty(PROPERTY_ERRORS, errors) }
        val view = resolveView(handlerMethod)
        val model = ModelAndView(view, exception.statusCode)
        model.addObject("target", exception.bindingResult.target)
        model.addObject(PROPERTY_PROBLEM_DETAIL, problemDetail)

        return model
    }

    private fun resolveView(handlerMethod: HandlerMethod): String {
        val classMapping = handlerMethod.method
            .declaringClass
            .getDeclaredAnnotation(RequestMapping::class.java)
            ?: return "500"
        val methodMapping = handlerMethod.getMethodAnnotation(RequestMapping::class.java) ?: return "500"
        var path = if (isFragment) "fragment" else "page"
        val classPath = classMapping.value.firstOrNull()
        val methodPath = methodMapping.value.firstOrNull()

        if (classPath !== null) {
            path += "/$classPath"
        }

        if (methodPath !== null) {
            path += "/$methodPath"
        }

        if (classPath === null && methodPath === null) {
            path += "/index"
        }

        return path
    }
}
