package com.leijendary.spring.microservicetemplate.error;

import com.leijendary.spring.microservicetemplate.data.response.GlobalExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Locale.getDefault;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalExceptionResponse> catchException(final Exception exception) {
        final var error = messageSource.getMessage("error.generic", new Object[0], getDefault());
        final var response = new GlobalExceptionResponse(error, exception.getMessage());

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
