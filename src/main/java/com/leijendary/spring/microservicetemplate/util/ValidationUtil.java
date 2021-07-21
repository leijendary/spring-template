package com.leijendary.spring.microservicetemplate.util;

import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public abstract class ValidationUtil extends ValidationUtils {

    public static void rejectIfEmptyOrWhitespace(final Errors errors, final String field) {
        rejectIfEmptyOrWhitespace(errors, field, "validation.required");
    }

    public static void rejectIfMaxLength(final Errors errors, final String field, final int length) {
        Assert.notNull(errors, "Errors object must not be null");

        // Skip validation if there is already an error in the field
        if (errors.hasFieldErrors(field)) {
            return;
        }

        final var value = errors.getFieldValue(field);

        if (value != null && value.toString().length() > length) {
            errors.rejectValue(field, "validation.maxLength", new Object[] { length }, null);
        }
    }
}
