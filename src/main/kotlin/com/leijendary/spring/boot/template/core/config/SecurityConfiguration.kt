package com.leijendary.spring.boot.template.core.config

import com.leijendary.spring.boot.template.core.config.properties.AuthProperties
import com.leijendary.spring.boot.template.core.security.AppAuthenticationEntryPoint
import com.leijendary.spring.boot.template.core.security.AudienceValidator
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val appAuthenticationEntryPoint: AppAuthenticationEntryPoint,
    private val authProperties: AuthProperties,
    private val oAuth2ResourceServerProperties: OAuth2ResourceServerProperties,
) : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .headers()
            .disable()
            .authorizeRequests()
            .anyRequest().permitAll()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .anonymous { it.principal(authProperties.anonymousUser.principal) }
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .oauth2ResourceServer()
            .authenticationEntryPoint(appAuthenticationEntryPoint)
            .jwt()
            .decoder(jwtDecoder())
    }

    private fun jwtDecoder(): JwtDecoder {
        val audience = authProperties.audience
        val withAudience = AudienceValidator(audience)
        val defaultValidator = JwtValidators.createDefault()
        val validator = DelegatingOAuth2TokenValidator(withAudience, defaultValidator)
        val jwkSetUri = oAuth2ResourceServerProperties.jwt.jwkSetUri
        val jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build()
        jwtDecoder.setJwtValidator(validator)

        return jwtDecoder
    }
}