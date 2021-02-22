package com.leijendary.spring.microservicetemplate.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryRequest {

    private String query;
}
