package com.leijendary.spring.microservicetemplate.error;

import com.leijendary.spring.microservicetemplate.data.response.ValidationResponse;
import com.leijendary.spring.microservicetemplate.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

import static java.util.Locale.getDefault;
import static java.util.Optional.ofNullable;

@RestControllerAdvice
@Order(4)
@RequiredArgsConstructor
public class ValidationExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationResponse> catchValidation(final ValidationException exception) {
        final var validations = new HashMap<String, String>();
        final var message = messageSource.getMessage("validation.message", new Object[0], getDefault());
        final var error = messageSource.getMessage("validation.error", new Object[0], getDefault());

        exception.getErrors().forEach(e -> {
            final var field = e.getField();
            final var code = e.getCode();
            final var args = e.getArguments();
            final var defaultMessage = e.getDefaultMessage();
            final var m = ofNullable(code)
                    .map(s -> messageSource.getMessage(s, args, defaultMessage, getDefault()))
                    .orElse(null);

            validations.put(field, m);
        });

        final var response = new ValidationResponse(error, message, validations);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
