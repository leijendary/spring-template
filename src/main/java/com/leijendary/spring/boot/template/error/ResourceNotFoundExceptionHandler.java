package com.leijendary.spring.boot.template.error;

import static com.leijendary.spring.boot.template.util.RequestContext.getLocale;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.leijendary.spring.boot.template.data.response.ErrorResponse;
import com.leijendary.spring.boot.template.exception.ResourceNotFoundException;

import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(3)
@RequiredArgsConstructor
public class ResourceNotFoundExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse catchResourceNotFound(final ResourceNotFoundException exception) {
        final var code = "error.resource.notFound";
        final var arguments = new Object[] { exception.getResource(), exception.getIdentifier() };
        final var message = messageSource.getMessage(code, arguments, getLocale());

        return ErrorResponse.builder()
                .addError(exception.getResource(), code, message)
                .status(NOT_FOUND)
                .build();
    }
}
