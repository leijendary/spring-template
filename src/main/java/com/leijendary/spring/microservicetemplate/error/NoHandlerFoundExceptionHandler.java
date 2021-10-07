package com.leijendary.spring.microservicetemplate.error;

import com.leijendary.spring.microservicetemplate.data.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.leijendary.spring.microservicetemplate.util.RequestContext.getLocale;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Order(1)
@RequiredArgsConstructor
public class NoHandlerFoundExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse catchNoHandlerFound(final NoHandlerFoundException exception) {
        final var code = "error.mapping.notFound";
        final var message = messageSource.getMessage(code, new Object[0], getLocale());

        return ErrorResponse.builder()
                .addError("Request URL", code, message + ": " + exception.getMessage())
                .status(NOT_FOUND)
                .build();
    }
}
