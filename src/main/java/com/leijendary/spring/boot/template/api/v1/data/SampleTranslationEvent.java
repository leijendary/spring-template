package com.leijendary.spring.boot.template.api.v1.data;

import com.leijendary.spring.boot.template.core.data.LocaleData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleTranslationEvent extends LocaleData {

    private String name;
    private String description;
}