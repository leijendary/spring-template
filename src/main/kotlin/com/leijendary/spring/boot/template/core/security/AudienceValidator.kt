package com.leijendary.spring.boot.template.core.security

import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_TOKEN
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.failure
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult.success
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.util.Assert

class AudienceValidator(audience: String) : OAuth2TokenValidator<Jwt> {
    private val audience: String

    init {
        Assert.hasText(audience, "Audience is null or empty")
        this.audience = audience
    }

    override fun validate(jwt: Jwt): OAuth2TokenValidatorResult {
        val audiences = jwt.audience

        if (audiences.contains(audience)) {
            return success()
        }

        val error = OAuth2Error(INVALID_TOKEN)

        return failure(error)
    }
}