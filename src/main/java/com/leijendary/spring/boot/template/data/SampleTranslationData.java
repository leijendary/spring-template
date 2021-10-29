package com.leijendary.spring.boot.template.data;

import com.leijendary.spring.boot.core.data.LocaleData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleTranslationData extends LocaleData {

    private String name;
    private String description;
}
