package com.leijendary.spring.boot.template.error;

import static com.leijendary.spring.boot.template.util.RequestContext.getLocale;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.leijendary.spring.boot.template.data.response.ErrorResponse;

import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(1)
@RequiredArgsConstructor
public class AccessDeniedExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    public ErrorResponse catchAccessDenied(final AccessDeniedException exception) {
        final var code = "access.denied";
        final var arguments = new Object[0];
        final var message = messageSource.getMessage(code, arguments, getLocale());

        return ErrorResponse.builder()
                .addError("Authorization", code, message + ": " + exception.getMessage())
                .status(FORBIDDEN)
                .build();
    }
}
