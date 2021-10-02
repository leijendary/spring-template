package com.leijendary.spring.microservicetemplate.data.response.v1;

import com.leijendary.spring.microservicetemplate.data.LocalizedData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;

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
