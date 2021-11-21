package com.leijendary.spring.boot.template.api.v1.data;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class SampleSearchResponse implements Serializable {

    private UUID id;
    private String column1;
    private String column2;
    private String name;
    private String description;
    private OffsetDateTime createdAt;
}
