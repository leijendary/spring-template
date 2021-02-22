package com.leijendary.spring.microservicetemplate.error;

import com.leijendary.spring.microservicetemplate.data.response.AccessDeniedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Locale.getDefault;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
@Order(1)
@RequiredArgsConstructor
public class AccessDeniedExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    public AccessDeniedResponse catchAccessDenied(AccessDeniedException exception) {
        final var message = messageSource.getMessage("access.denied", new Object[0], getDefault());
        final var error = messageSource.getMessage("access.error", new Object[0], getDefault());

        return new AccessDeniedResponse(error, message + ": " + exception.getMessage());
    }
}
