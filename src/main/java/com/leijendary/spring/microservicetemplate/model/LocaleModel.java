package com.leijendary.spring.microservicetemplate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class LocaleModel<R> extends IdentityIdModel {

    @ManyToOne
    @JoinColumn(name = "reference_id")
    private R reference;

    private String language;
    private int ordinal;
}
