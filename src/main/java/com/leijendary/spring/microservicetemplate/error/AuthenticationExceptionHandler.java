package com.leijendary.spring.microservicetemplate.error;

import com.leijendary.spring.microservicetemplate.data.response.AuthenticationExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Locale.getDefault;
import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
@Order(1)
@RequiredArgsConstructor
public class AuthenticationExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<AuthenticationExceptionResponse> catchAccessDenied(final AuthenticationException exception) {
        final var message = messageSource.getMessage("access.unauthorized", new Object[0], getDefault());
        final var error = messageSource.getMessage("access.error", new Object[0], getDefault());
        final var response =  new AuthenticationExceptionResponse(error,
                message + ": " + exception.getMessage());

        return status(response.getStatus()).body(response);
    }
}
