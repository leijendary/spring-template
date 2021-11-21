package com.leijendary.spring.boot.template.api.v1.data;

import com.leijendary.spring.boot.template.data.LocalizedData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleEvent extends LocalizedData<SampleTranslationEvent> {

    private String column1;
    private int column2;
}