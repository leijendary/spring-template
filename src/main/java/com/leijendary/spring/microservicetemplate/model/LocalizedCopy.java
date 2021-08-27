package com.leijendary.spring.microservicetemplate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Data
@MappedSuperclass
public abstract class LocalizedCopy<R, T extends LocaleCopy<R>> {

    @Id
    private long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "reference", cascade = ALL)
    private Set<T> translations = new HashSet<>();
}
