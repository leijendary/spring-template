package com.leijendary.spring.microservicetemplate.error;

import com.leijendary.spring.microservicetemplate.data.response.ResourceNotFoundResponse;
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
    public ResourceNotFoundResponse catchResourceNotFound(ResourceNotFoundException exception) {
        final var message = messageSource.getMessage("error.resource.not-found",
                new Object[] { exception.getResource(), exception.getIdentifier() }, getDefault());
        final var error = messageSource.getMessage("error.not-found", new Object[0], getDefault());

        return new ResourceNotFoundResponse(error, message);
    }
}
