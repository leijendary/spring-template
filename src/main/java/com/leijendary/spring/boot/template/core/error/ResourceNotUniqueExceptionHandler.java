package com.leijendary.spring.boot.template.core.error;

import static com.leijendary.spring.boot.template.core.util.RequestContext.getLocale;
import static org.springframework.http.HttpStatus.CONFLICT;

import com.leijendary.spring.boot.template.core.data.ErrorResponse;
import com.leijendary.spring.boot.template.core.exception.ResourceNotUniqueException;

import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(3)
@RequiredArgsConstructor
public class ResourceNotUniqueExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ResourceNotUniqueException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse catchResourceNotUnique(final ResourceNotUniqueException exception) {
        final var code = "validation.alreadyExists";
        final var arguments = new Object[] { exception.getField(), exception.getValue() };
        final var message = messageSource.getMessage(code, arguments, getLocale());

        return ErrorResponse.builder()
                .addError(exception.getField(), code, message)
                .status(CONFLICT)
                .build();
    }
}