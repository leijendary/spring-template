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
    @OneToMany(mappedBy = "reference", fetch = EAGER, cascade = ALL, orphanRemoval = true)
    private Set<T> translations = new HashSet<>();

    public LocalizedModel<R, T> setTranslations(final Set<T> translations) {
        // clear() and addAll() to keep hibernate reference
        this.translations.clear();
        this.translations.addAll(translations);

        return this;
    }
}
