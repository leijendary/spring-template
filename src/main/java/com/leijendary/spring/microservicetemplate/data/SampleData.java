package com.leijendary.spring.microservicetemplate.data;

import lombok.Data;

import java.util.Set;

@Data
public class SampleData {

    private String column1;
    private int column2;
    private Set<SampleTranslationData> translations;
}
