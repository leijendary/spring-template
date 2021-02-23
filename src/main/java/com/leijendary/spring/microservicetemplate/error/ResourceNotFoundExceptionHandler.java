package com.leijendary.spring.microservicetemplate.error;

import com.leijendary.spring.microservicetemplate.data.response.ResourceNotFoundResponse;
import com.leijendary.spring.microservicetemplate.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.Locale.getDefault;
import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
@Order(3)
@RequiredArgsConstructor
public class ResourceNotFoundExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResourceNotFoundResponse> catchResourceNotFound(final ResourceNotFoundException exception) {
        final var message = messageSource.getMessage("error.resource.not-found",
                new Object[] { exception.getResource(), exception.getIdentifier() }, getDefault());
        final var error = messageSource.getMessage("error.not-found", new Object[0], getDefault());
        final var response = new ResourceNotFoundResponse(error, message);

        return status(response.getStatus()).body(response);
    }
}
