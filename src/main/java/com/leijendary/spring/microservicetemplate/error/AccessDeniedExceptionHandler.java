package com.leijendary.spring.microservicetemplate.error;

import com.leijendary.spring.microservicetemplate.data.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.leijendary.spring.microservicetemplate.util.RequestContextUtil.getLocale;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
@Order(1)
@RequiredArgsConstructor
public class AccessDeniedExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    public ErrorResponse catchAccessDenied(final AccessDeniedException exception) {
        final var code = "access.denied";
        final var message = messageSource.getMessage(code, new Object[0], getLocale());

        return ErrorResponse.builder()
                .addError("Authorization", code, message + ": " + exception.getMessage())
                .status(FORBIDDEN)
                .build();
    }
}
