package com.leijendary.spring.microservicetemplate.error;

import com.leijendary.spring.microservicetemplate.data.response.AccessDeniedResponse;
import com.leijendary.spring.microservicetemplate.data.response.InsufficientScopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Locale.getDefault;
import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
@Order(1)
@RequiredArgsConstructor
public class AccessDeniedExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<AccessDeniedResponse> catchAccessDenied(final AccessDeniedException exception) {
        final var message = messageSource.getMessage("access.denied", new Object[0], getDefault());
        final var error = messageSource.getMessage("access.error", new Object[0], getDefault());
        var response = new AccessDeniedResponse(error, message + ": " + exception.getMessage());

        // For Insufficient Scope errors
        if (exception.getCause() instanceof InsufficientScopeException) {
            final var cause = (InsufficientScopeException) exception.getCause();
            final var scopes = cause.getAdditionalInformation()
                    .get("scope")
                    .split("\\s+");

            response = new InsufficientScopeResponse(error, exception.getMessage(), scopes);
        }

        return status(response.getStatus()).body(response);
    }
}
