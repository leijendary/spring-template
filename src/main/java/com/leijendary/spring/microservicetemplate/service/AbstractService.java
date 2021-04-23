package com.leijendary.spring.microservicetemplate.service;

import com.leijendary.spring.microservicetemplate.exception.ValidationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

public abstract class AbstractService {

    protected <T> void validate(Validator validator, Object target, Class<T> tClass) {
        final var errors = new BeanPropertyBindingResult(target, tClass.getName());

        validator.validate(target, errors);

        if (errors.hasErrors()) {
            throw new ValidationException(errors.getFieldErrors());
        }
    }
}
