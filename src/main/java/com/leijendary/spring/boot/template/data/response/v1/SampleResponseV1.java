package com.leijendary.spring.boot.template.data.response.v1;

import java.time.OffsetDateTime;

import com.leijendary.spring.boot.template.data.LocalizedData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleResponseV1 extends LocalizedData<SampleTableTranslationResponseV1> {

    private String column1;
    private String column2;
    private OffsetDateTime createdDate;
    private String createdBy;
    private OffsetDateTime lastModifiedDate;
    private String lastModifiedBy;
}
