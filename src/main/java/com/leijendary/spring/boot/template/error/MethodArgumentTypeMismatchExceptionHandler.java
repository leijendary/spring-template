package com.leijendary.spring.boot.template.error;

import static com.leijendary.spring.boot.template.util.RequestContext.getLocale;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.util.ClassUtils.getDescriptiveType;

import com.leijendary.spring.boot.template.data.ErrorResponse;

import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@Order(4)
@RequiredArgsConstructor
public class MethodArgumentTypeMismatchExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse catchMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException exception) {
        final var code = "error.invalid.type";
        final var name = exception.getName();
        final var parameter = exception.getParameter();
        final var requiredType = parameter.getParameterType().getSimpleName();
        final var value = exception.getValue();
        final var valueType = ofNullable(getDescriptiveType(value))
                .map(ClassUtils::getShortName)
                .orElse(null);
        final var arguments = new Object[] { name, value, valueType, requiredType };
        final var message = messageSource.getMessage(code, arguments, getLocale());

        return ErrorResponse.builder()
                .addError(name, code, message)
                .status(BAD_REQUEST)
                .build();
    }
}
