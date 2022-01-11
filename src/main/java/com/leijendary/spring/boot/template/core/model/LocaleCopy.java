package com.leijendary.spring.boot.template.core.model;

import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class LocaleCopy extends AppModel {

    private String language;
    private int ordinal;
}
