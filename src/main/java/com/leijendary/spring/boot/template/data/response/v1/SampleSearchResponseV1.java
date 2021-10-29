package com.leijendary.spring.boot.template.data.response.v1;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class SampleSearchResponseV1 implements Serializable {

    private UUID id;
    private String column1;
    private String column2;
    private String name;
    private String description;
    private OffsetDateTime createdDate;
}
