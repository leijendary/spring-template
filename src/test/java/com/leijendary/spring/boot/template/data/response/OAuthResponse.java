package com.leijendary.spring.boot.template.data.response;

import lombok.Data;

@Data
public class OAuthResponse {

    private String accessToken;
    private String scope;
    private int expiresIn;
    private String tokenType;
}
