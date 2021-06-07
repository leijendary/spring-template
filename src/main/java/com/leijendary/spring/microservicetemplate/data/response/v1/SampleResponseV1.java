package com.leijendary.spring.microservicetemplate.data.response.v1;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class SampleResponseV1 implements Serializable {

    private UUID id;
    private String column1;
    private String column2;
}
