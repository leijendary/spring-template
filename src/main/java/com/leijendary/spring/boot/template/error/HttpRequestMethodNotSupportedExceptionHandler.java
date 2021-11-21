package com.leijendary.spring.boot.template.error;

import static com.leijendary.spring.boot.template.util.RequestContext.getLocale;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import com.leijendary.spring.boot.template.data.ErrorResponse;

import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(4)
@RequiredArgsConstructor
public class HttpRequestMethodNotSupportedExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ErrorResponse catchHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException exception) {
        final var code = "error.method.notSupported";
        final var arguments = new Object[] { exception.getMethod(), exception.getSupportedHttpMethods() };
        final var message = messageSource.getMessage(code, arguments, getLocale());

        return ErrorResponse.builder()
                .addError("HTTP Method", code, message)
                .status(METHOD_NOT_ALLOWED)
                .build();
    }
}
