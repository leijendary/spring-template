package com.leijendary.spring.microservicetemplate.data.response.v1;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
public class SampleSearchResponseV1 implements Serializable {

    private long id;
    private String column1;
    private String column2;
    private String name;
    private String description;
    private OffsetDateTime createdDate;
}
