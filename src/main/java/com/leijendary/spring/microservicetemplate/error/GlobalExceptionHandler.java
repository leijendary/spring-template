package com.leijendary.spring.microservicetemplate.error;

import com.leijendary.spring.microservicetemplate.data.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Locale.getDefault;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse catchException(final Exception exception) {
        final var code = "error.generic";
        final var error = messageSource.getMessage(code, new Object[0], getDefault());

        return ErrorResponse.builder()
                .addError(error, code, exception.getMessage())
                .build();
    }
}
