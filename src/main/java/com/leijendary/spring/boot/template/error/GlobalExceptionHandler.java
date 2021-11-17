package com.leijendary.spring.boot.template.error;

import static com.leijendary.spring.boot.template.util.RequestContext.getLocale;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.leijendary.spring.boot.template.data.response.ErrorResponse;

import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Order
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse catchException(final Exception exception) {
        log.error("Global Exception", exception);

        final var code = "error.generic";
        final var arguments = new Object[0];
        final var error = messageSource.getMessage(code, arguments, getLocale());

        return ErrorResponse.builder()
                .addError(error, code, exception.getMessage())
                .build();
    }
}
