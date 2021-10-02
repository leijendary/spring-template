package com.leijendary.spring.microservicetemplate.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleTranslationData extends LocaleData {

    private String name;
    private String description;
}
