package com.leijendary.spring.boot.template.error;

import static com.leijendary.spring.boot.template.util.RequestContext.getLocale;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.leijendary.spring.boot.template.data.response.ErrorResponse;

import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(1)
@RequiredArgsConstructor
public class NoHandlerFoundExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse catchNoHandlerFound(final NoHandlerFoundException exception) {
        final var code = "error.mapping.notFound";
        final var arguments = new Object[] { exception.getMessage() };
        final var message = messageSource.getMessage(code, arguments, getLocale());

        return ErrorResponse.builder()
                .addError("Request URL", code, message)
                .status(NOT_FOUND)
                .build();
    }
}
