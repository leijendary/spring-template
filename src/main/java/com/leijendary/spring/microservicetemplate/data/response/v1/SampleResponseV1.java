package com.leijendary.spring.microservicetemplate.data.response.v1;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;

@Data
public class SampleResponseV1 implements Serializable {

    private long id;
    private String column1;
    private String column2;
    private OffsetDateTime createdDate;
    private String createdBy;
    private OffsetDateTime lastModifiedDate;
    private String lastModifiedBy;
    // LinkedHashSet to maintain insertion order
    private LinkedHashSet<SampleTableTranslationResponseV1> translations = new LinkedHashSet<>();
}
