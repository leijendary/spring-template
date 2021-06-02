package com.leijendary.spring.microservicetemplate.error;

import com.leijendary.spring.microservicetemplate.data.response.ErrorResponse;
import com.leijendary.spring.microservicetemplate.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Locale.getDefault;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Order(4)
@RequiredArgsConstructor
public class ValidationExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse catchValidation(final ValidationException exception) {
        final var response = ErrorResponse.builder()
                .status(BAD_REQUEST);

        exception.getErrors().forEach(e -> {
            final var field = e.getField();
            final var code = e.getCode();
            final var args = e.getArguments();
            final var defaultMessage = e.getDefaultMessage();
            final var message = ofNullable(code)
                    .map(s -> messageSource.getMessage(s, args, defaultMessage, getDefault()))
                    .orElse(null);

            response.addError(field, code, message);
        });

        return response.build();
    }
}
