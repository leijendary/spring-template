package com.leijendary.spring.microservicetemplate.error;

import com.leijendary.spring.microservicetemplate.data.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Locale.getDefault;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Order(4)
@RequiredArgsConstructor
public class MethodArgumentNotValidExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse catchMethodArgumentNotValid(final MethodArgumentNotValidException exception) {
        final var response = ErrorResponse.builder()
                .status(BAD_REQUEST);

        exception.getAllErrors()
                .stream()
                .map(objectError -> (FieldError) objectError)
                .forEach(e -> {
                    final var objectName = e.getField();
                    final var code = e.getCode();
                    final var args = e.getArguments();
                    final var defaultMessage = e.getDefaultMessage();
                    final var message = ofNullable(defaultMessage)
                            .map(s -> messageSource.getMessage(s, args, defaultMessage, getDefault()))
                            .orElse(null);

                    response.addError(objectName, code, message);
                });

        return response.build();
    }
}
