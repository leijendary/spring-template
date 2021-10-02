package com.leijendary.spring.microservicetemplate.model;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class LocaleCopy extends AbstractModel {

    private String language;
    private int ordinal;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocaleCopy that = (LocaleCopy) o;

        return language.equals(that.language);
    }

    @Override
    public int hashCode() {
        return language.hashCode();
    }
}
