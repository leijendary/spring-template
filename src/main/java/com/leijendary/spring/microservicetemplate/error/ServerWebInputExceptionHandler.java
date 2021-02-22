package com.leijendary.spring.microservicetemplate.error;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.leijendary.spring.microservicetemplate.data.response.ServerWebInputResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;

import static java.util.Locale.getDefault;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

@RestControllerAdvice
@Order(2)
@RequiredArgsConstructor
public class ServerWebInputExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ServerWebInputResponse> catchServerWebInput(ServerWebInputException exception) {
        final var error = exception.getReason();
        final var message = getMessage(exception);
        final var status = exception.getStatus().value();

        return ResponseEntity.status(status)
                .body(new ServerWebInputResponse(error, message, status));
    }

    private String getMessage(ServerWebInputException exception) {
        // JSON decoding error: Cannot deserialize value of type `int` from String "one": not a valid Integer value
        final var isDecodingException = exception.getCause() instanceof DecodingException;
        var message = exception.getMessage();

        if (isDecodingException) {
            message = getMessage((DecodingException) exception.getCause());
        }

        return message;
    }

    private String getMessage(DecodingException exception) {
        final var isInvalidFormat = exception.getCause() instanceof InvalidFormatException;
        final var isJsonMapping = exception.getCause() instanceof JsonMappingException;
        var message = ofNullable(exception.getMessage())
                .map(s -> s.replace("JSON decoding error: ", ""))
                .orElse(exception.getMessage());

        if (isInvalidFormat) {
            message = getMessage((InvalidFormatException) exception.getCause());
        }

        if (isJsonMapping) {
            message = getMessage((JsonMappingException) exception.getCause());
        }

        return message;
    }

    private String getMessage(InvalidFormatException exception) {
        final var path = exception.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(joining("."));

        return messageSource.getMessage("error.invalid-format",
                new Object[] { path, exception.getValue(), exception.getTargetType().getSimpleName() }, getDefault());
    }

    private String getMessage(JsonMappingException exception) {
        return exception.getOriginalMessage();
    }
}
