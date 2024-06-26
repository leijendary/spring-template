package com.leijendary.error

import com.leijendary.extension.logger
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.async.AsyncRequestNotUsableException

@RestControllerAdvice
@Order(1)
class AsyncRequestNotUsableExceptionHandler {
    private val log = logger()

    @ExceptionHandler(AsyncRequestNotUsableException::class)
    fun handleAsyncRequestNotUsableException(request: HttpServletRequest) {
        log.warn("Request not usable for ${request.method} ${request.requestURI} from address ${request.remoteAddr}")
    }
}
