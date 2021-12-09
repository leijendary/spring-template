package com.leijendary.spring.boot.template.model;

import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class LocaleModel extends AppModel {

    private String language;
    private int ordinal;
}
