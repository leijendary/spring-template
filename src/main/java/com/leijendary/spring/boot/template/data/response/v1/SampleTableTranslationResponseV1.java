package com.leijendary.spring.boot.template.data.response.v1;

import com.leijendary.spring.boot.core.data.LocaleData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleTableTranslationResponseV1 extends LocaleData {

    private String name;
    private String description;
}
