package com.leijendary.spring.boot.template.core.error;

import static com.leijendary.spring.boot.template.core.util.RequestContext.getLocale;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.leijendary.spring.boot.template.core.data.ErrorResponse;

import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(4)
@RequiredArgsConstructor
public class MethodArgumentNotValidExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse catchMethodArgumentNotValid(final MethodArgumentNotValidException exception) {
        final var response = ErrorResponse.builder().status(BAD_REQUEST);

        exception.getAllErrors().forEach(e -> {
            String objectName;

            if (e instanceof FieldError) {
                objectName = ((FieldError) e).getField();
            } else {
                objectName = e.getObjectName();
            }

            final var code = e.getDefaultMessage();
            final var arguments = e.getArguments();
            final var message = ofNullable(code)
                    .map(s -> messageSource.getMessage(s, arguments, code, getLocale()))
                    .orElse(null);

            response.addError(objectName, code, message);
        });

        return response.build();
    }
}
