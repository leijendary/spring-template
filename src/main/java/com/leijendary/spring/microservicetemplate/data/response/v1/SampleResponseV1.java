package com.leijendary.spring.microservicetemplate.data.response.v1;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class SampleResponseV1 implements Serializable {

    private long id;
    private String column1;
    private String column2;
    private Instant createdDate;
    private String createdBy;
    private Instant lastModifiedDate;
    private String lastModifiedBy;
}
