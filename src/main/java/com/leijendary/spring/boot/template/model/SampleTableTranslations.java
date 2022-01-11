package com.leijendary.spring.boot.template.model;

import javax.persistence.Embeddable;

import com.leijendary.spring.boot.template.core.model.LocaleModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Embeddable
public class SampleTableTranslations extends LocaleModel {

    private String name;
    private String description;
}
