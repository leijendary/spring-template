package com.leijendary.spring.microservicetemplate.error;

import com.leijendary.spring.microservicetemplate.data.response.ErrorResponse;
import com.leijendary.spring.microservicetemplate.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Locale.getDefault;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Order(3)
@RequiredArgsConstructor
public class ResourceNotFoundExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse catchResourceNotFound(final ResourceNotFoundException exception) {
        final var code = "error.resource.notFound";
        final var message = messageSource.getMessage(code,
                new Object[] { exception.getResource(), exception.getIdentifier() }, getDefault());

        return ErrorResponse.builder()
                .addError(exception.getResource(), code, message)
                .status(NOT_FOUND)
                .build();
    }
}
