package com.leijendary.spring.microservicetemplate.data.response.v1;

import lombok.Data;

import java.io.Serializable;

@Data
public class SampleTableTranslationResponseV1 implements Serializable {

    private String name;
    private String description;
    private String language;
    private int ordinal;
}
