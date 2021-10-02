package com.leijendary.spring.microservicetemplate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Embeddable;

@Data
@EqualsAndHashCode(callSuper = true)
@Embeddable
public class SampleTableTranslations extends LocaleModel {

    private String name;
    private String description;
}
