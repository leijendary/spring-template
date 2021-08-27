package com.leijendary.spring.microservicetemplate.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedHashSet;

@Data
public abstract class LocalizedData<T> implements Serializable {

    private long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private LinkedHashSet<T> translations = new LinkedHashSet<>();
}
