package com.leijendary.spring.boot.template.core.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.oauth2.jwt.JwtValidators.createDefault;
import static org.springframework.security.oauth2.jwt.NimbusJwtDecoder.withJwkSetUri;

import com.leijendary.spring.boot.template.core.config.properties.AuthProperties;
import com.leijendary.spring.boot.template.core.security.AppAuthenticationEntryPoint;
import com.leijendary.spring.boot.template.core.security.AudienceValidator;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AppAuthenticationEntryPoint appAuthenticationEntryPoint;
    private final AuthProperties authProperties;
    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .headers()
                .disable()
                .authorizeRequests()
                .anyRequest().permitAll()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
            .and()
                .anonymous(configurer -> configurer.principal(authProperties.getAnonymousUser().getPrincipal()))
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .oauth2ResourceServer()
                .authenticationEntryPoint(appAuthenticationEntryPoint)
                .jwt()
                .decoder(jwtDecoder());
    }

    private JwtDecoder jwtDecoder() {
        final var audience = authProperties.getAudience();
        final var withAudience = new AudienceValidator(audience);
        final var defaultValidator = createDefault();
        final var validator = new DelegatingOAuth2TokenValidator<>(withAudience, defaultValidator);
        final var jwkSetUri = oAuth2ResourceServerProperties.getJwt().getJwkSetUri();
        final var jwtDecoder = withJwkSetUri(jwkSetUri).build();
        jwtDecoder.setJwtValidator(validator);

        return jwtDecoder;
    }
}
