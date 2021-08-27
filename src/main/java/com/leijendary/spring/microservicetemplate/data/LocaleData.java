package com.leijendary.spring.microservicetemplate.data;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class LocaleData implements Serializable {

    private long id;
    private String language;
    private int ordinal;
}
