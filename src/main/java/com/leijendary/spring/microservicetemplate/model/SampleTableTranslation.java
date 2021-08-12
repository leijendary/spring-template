package com.leijendary.spring.microservicetemplate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class SampleTableTranslation extends LocaleModel<SampleTable> {

    private String name;
    private String description;
}
