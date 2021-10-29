package com.leijendary.spring.boot.template.model;

import com.leijendary.spring.boot.core.model.LocaleModel;
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
