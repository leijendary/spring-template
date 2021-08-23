package com.leijendary.spring.microservicetemplate.error;

import com.leijendary.spring.microservicetemplate.data.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.leijendary.spring.microservicetemplate.util.RequestContext.getLocale;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

@RestControllerAdvice
@Order(4)
@RequiredArgsConstructor
public class HttpRequestMethodNotSupportedExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ErrorResponse catchHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException exception) {
        final var code = "error.method.notSupported";
        final var source = "httpMethod";
        final var message = messageSource.getMessage(code,
                new Object[] { exception.getMethod(), exception.getSupportedHttpMethods() }, getLocale());

        return ErrorResponse.builder()
                .addError(source, code, message)
                .status(METHOD_NOT_ALLOWED)
                .build();
    }
}
