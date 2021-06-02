package com.leijendary.spring.microservicetemplate.error;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.leijendary.spring.microservicetemplate.data.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

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
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse catchHttpMessageNotReadable(final HttpMessageNotReadableException exception) {
        final var response = ErrorResponse.builder()
                .status(BAD_REQUEST);

        errors(exception, response);

        return response.build();
    }

    private void errors(final HttpMessageNotReadableException exception,
                        final ErrorResponse.ErrorResponseBuilder errorResponse) {
        final var source = "Request Body";
        final var code = "error.bad-request";
        var message = ofNullable(exception.getMessage())
                .orElse("");

        if (message.startsWith("Required request body is missing")) {
            errorResponse.addError(source, code, message.split(":")[0]);

            return;
        }

        if (exception.getCause() instanceof InvalidFormatException) {
            errors((InvalidFormatException) exception.getCause(), errorResponse);

            return;
        }

        if (exception.getCause() instanceof JsonMappingException) {
            errors((JsonMappingException) exception.getCause(), errorResponse);

            return;
        }

        message = ofNullable(exception.getMessage())
                .map(s -> s.replace("JSON decoding error: ", ""))
                .orElse(exception.getMessage());

        errorResponse.addError(source, code, message);
    }

    private void errors(final InvalidFormatException exception,
                        final ErrorResponse.ErrorResponseBuilder errorResponse) {
        final var path = getPath(exception.getPath());
        final var code = "error.invalid-format";
        final var message =  messageSource.getMessage(code,
                new Object[] { path, exception.getValue(), exception.getTargetType().getSimpleName() }, getDefault());

        errorResponse.addError(path, code, message);
    }

    private void errors(final JsonMappingException exception,
                            final ErrorResponse.ErrorResponseBuilder errorResponse) {
        errorResponse.addError(getPath(exception.getPath()), "error.invalid-format",
                exception.getOriginalMessage());
    }

    private String getPath(List<Reference> path) {
        return path.stream()
                .map(Reference::getFieldName)
                .collect(joining("."));
    }
}
