package com.leijendary.spring.boot.template.core.error;

import static com.leijendary.spring.boot.template.core.util.RequestContext.getLocale;
import static com.leijendary.spring.boot.template.core.util.StringUtil.snakeCaseToCamelCase;
import static org.apache.commons.lang3.StringUtils.substringBetween;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.leijendary.spring.boot.template.core.data.ErrorResponse;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(3)
@RequiredArgsConstructor
public class DataIntegrityViolationExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> catchDataIntegrityViolation(final DataIntegrityViolationException exception) {
        final var errorResponse = getResponse(exception);
        final var status = errorResponse.getMeta().get("status");

        return ResponseEntity
                .status((int) status)
                .body(errorResponse);
    }

    private ErrorResponse getResponse(final DataIntegrityViolationException exception) {
        final var cause = exception.getCause();

        if (cause instanceof ConstraintViolationException) {
            return constraintViolationException((ConstraintViolationException) cause);
        }

        final var code = "error.dataIntegrity";
        final var arguments = new Object[] { exception.getMessage() };
        final var message = messageSource.getMessage(code, arguments, getLocale());

        return ErrorResponse.builder()
                .addError("Entity", code, message)
                .status(INTERNAL_SERVER_ERROR)
                .build();
    }

    private ErrorResponse constraintViolationException(final ConstraintViolationException exception) {
        final var errorMessage = exception.getSQLException().getNextException().getMessage();
        final var field = snakeCaseToCamelCase(substringBetween(errorMessage, "Key (", ")="));
        final var value = substringBetween(errorMessage, "=(", ") ");
        final var code = "validation.alreadyExists";
        final var arguments = new Object[] { field, value };
        final var message = messageSource.getMessage(code, arguments, getLocale());

        return ErrorResponse.builder()
                .addError(field, code, message)
                .status(CONFLICT)
                .build();
    }
}