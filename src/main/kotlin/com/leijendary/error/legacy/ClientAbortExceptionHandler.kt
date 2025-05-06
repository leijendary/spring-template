package com.leijendary.error.legacy

import com.leijendary.extension.logger
import jakarta.servlet.http.HttpServletRequest
import org.apache.catalina.connector.ClientAbortException
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
@Order(1)
class ClientAbortExceptionHandler {
    private val log = logger()

    @ExceptionHandler(ClientAbortException::class)
    fun handleClientAbortException(request: HttpServletRequest) {
        log.warn("Client aborted for ${request.method} ${request.requestURI} from address ${request.remoteAddr}")
    }
}
