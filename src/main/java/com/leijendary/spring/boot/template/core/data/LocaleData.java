package com.leijendary.spring.boot.template.core.data;

import java.io.Serializable;

import lombok.Data;

@Data
public abstract class LocaleData implements Serializable {

    private String language;
    private int ordinal;
}
