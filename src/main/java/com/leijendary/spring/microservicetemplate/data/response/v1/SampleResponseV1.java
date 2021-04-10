package com.leijendary.spring.microservicetemplate.data.response.v1;

import lombok.Data;

import java.io.Serializable;

@Data
public class SampleResponseV1 implements Serializable {

    private int id;
    private String column1;
    private String column2;
}
