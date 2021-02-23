package com.leijendary.spring.microservicetemplate.error;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.leijendary.spring.microservicetemplate.data.response.HttpMessageNotReadableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Locale.getDefault;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Order(2)
@RequiredArgsConstructor
public class HttpMessageNotReadableExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HttpMessageNotReadableResponse> catchHttpMessageNotReadable(
            final HttpMessageNotReadableException exception) {
        final var error = messageSource.getMessage("error.bad-request", new Object[0], getDefault());
        final var message = getMessage(exception);
        final var response = new HttpMessageNotReadableResponse(error, message);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    private String getMessage(final HttpMessageNotReadableException exception) {
        if (exception.getCause() instanceof InvalidFormatException) {
            return getMessage((InvalidFormatException) exception.getCause());
        }

        if (exception.getCause() instanceof JsonMappingException) {
            return getMessage((JsonMappingException) exception.getCause());
        }

        return ofNullable(exception.getMessage())
                .map(s -> s.replace("JSON decoding error: ", ""))
                .orElse(exception.getMessage());
    }

    private String getMessage(final InvalidFormatException exception) {
        final var path = exception.getPath().stream()
                .map(Reference::getFieldName)
                .collect(joining("."));

        return messageSource.getMessage("error.invalid-format",
                new Object[] { path, exception.getValue(), exception.getTargetType().getSimpleName() }, getDefault());
    }

    private String getMessage(final JsonMappingException exception) {
        return exception.getOriginalMessage();
    }
}
