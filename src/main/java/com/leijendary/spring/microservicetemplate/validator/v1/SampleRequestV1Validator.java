package com.leijendary.spring.microservicetemplate.validator.v1;

import com.leijendary.spring.microservicetemplate.data.request.v1.SampleRequestV1;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.leijendary.spring.microservicetemplate.util.ValidationUtil.rejectIfEmptyOrWhitespace;
import static com.leijendary.spring.microservicetemplate.util.ValidationUtil.rejectIfMaxLength;

@Component
public class SampleRequestV1Validator implements Validator {

    private static final String FIELD_FIELD_1 = "field1";
    private static final String FIELD_FIELD_2 = "field2";
    private static final int FIELD_1_LENGTH = 50;

    @Override
    public boolean supports(@NonNull Class<?> tClass) {
        return SampleRequestV1.class.equals(tClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {
        field1(errors);
        field2(errors);
    }

    private void field1(Errors errors) {
        rejectIfEmptyOrWhitespace(errors, FIELD_FIELD_1);
        rejectIfMaxLength(errors, FIELD_FIELD_1, FIELD_1_LENGTH);
    }

    private void field2(Errors errors) {
        rejectIfEmptyOrWhitespace(errors, FIELD_FIELD_2);
    }
}
