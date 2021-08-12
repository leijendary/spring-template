package com.leijendary.spring.microservicetemplate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class LocalizedModel<R, T extends LocaleModel<R>> extends IdentityIdModel {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "reference")
    private Set<T> translations;
}
