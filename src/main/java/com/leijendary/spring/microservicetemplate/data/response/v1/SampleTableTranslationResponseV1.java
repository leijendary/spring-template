package com.leijendary.spring.microservicetemplate.data.response.v1;

import com.leijendary.spring.microservicetemplate.data.LocaleData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleTableTranslationResponseV1 extends LocaleData {

    private String name;
    private String description;
}
