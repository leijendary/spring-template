package com.leijendary.spring.microservicetemplate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class LocalizedModel<R, T extends LocaleModel<R>> extends SnowflakeIdModel {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "reference", fetch = EAGER, cascade = ALL)
    private Set<T> translations = new HashSet<>();
}
