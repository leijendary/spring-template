package com.leijendary.spring.boot.template.api.v1.data;

import java.time.OffsetDateTime;

import com.leijendary.spring.boot.template.data.LocalizedData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SampleResponse extends LocalizedData<SampleTranslationResponse> {

    private String column1;
    private String column2;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime lastModifiedAt;
    private String lastModifiedBy;
}
