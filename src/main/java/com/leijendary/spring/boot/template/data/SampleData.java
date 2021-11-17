package com.leijendary.spring.boot.template.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleData extends LocalizedData<SampleTranslationData> {

    private String column1;
    private int column2;
}
