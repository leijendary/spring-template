package com.leijendary.spring.boot.template.core.security;

import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_TOKEN;
import static org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.failure;
import static org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.success;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final String audience;

    public AudienceValidator(final String audience) {
        Assert.hasText(audience, "Audience is null or empty");

        this.audience = audience;
    }

    @Override
    public OAuth2TokenValidatorResult validate(final Jwt jwt) {
        final var audiences = jwt.getAudience();

        if (audiences.contains(this.audience)) {
            return success();
        }

        final var error = new OAuth2Error(INVALID_TOKEN);

        return failure(error);
    }
}
