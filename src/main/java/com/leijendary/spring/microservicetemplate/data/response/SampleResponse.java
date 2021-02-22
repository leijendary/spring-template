package com.leijendary.spring.microservicetemplate.data.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class SampleResponse implements Serializable {

    private int id;
    private String column1;
    private String column2;
}
