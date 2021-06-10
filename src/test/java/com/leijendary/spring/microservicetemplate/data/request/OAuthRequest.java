package com.leijendary.spring.microservicetemplate.data.request;

import lombok.Data;

@Data
public class OAuthRequest {

    private String username;
    private String password;
    private String grantType = "password";
    private String audience = "http://localhost:8080";
    private String scope;
}
