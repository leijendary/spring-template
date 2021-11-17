package com.leijendary.spring.boot.template.model;

import javax.persistence.MappedSuperclass;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class LocaleModel extends AppModel {

    private String language;
    private int ordinal;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocaleModel that = (LocaleModel) o;

        return language.equals(that.language);
    }

    @Override
    public int hashCode() {
        return language.hashCode();
    }
}
