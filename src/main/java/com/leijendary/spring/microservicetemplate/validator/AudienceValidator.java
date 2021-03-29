package com.leijendary.spring.microservicetemplate.validator;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
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
            return OAuth2TokenValidatorResult.success();
        }

        final var error = new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN);

        return OAuth2TokenValidatorResult.failure(error);
    }
}
