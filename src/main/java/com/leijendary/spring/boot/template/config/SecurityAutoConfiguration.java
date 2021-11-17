package com.leijendary.spring.boot.template.config;

import static java.util.stream.Collectors.toList;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.oauth2.jwt.JwtValidators.createDefault;
import static org.springframework.security.oauth2.jwt.NimbusJwtDecoder.withJwkSetUri;

import com.leijendary.spring.boot.template.config.properties.AuthProperties;
import com.leijendary.spring.boot.template.config.properties.CorsProperties;
import com.leijendary.spring.boot.template.security.AppAuthenticationEntryPoint;
import com.leijendary.spring.boot.template.security.AudienceValidator;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

    private final AppAuthenticationEntryPoint appAuthenticationEntryPoint;
    private final AuthProperties authProperties;
    private final CorsProperties corsProperties;
    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().permitAll()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
            .and()
                .cors()
                .configurationSource(corsConfigurationSource())
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

    private CorsConfigurationSource corsConfigurationSource() {
        final var config = new CorsConfiguration();
        config.setAllowedOrigins(corsProperties.getAllowedOrigins());
        config.setAllowedOriginPatterns(corsProperties.getAllowedOriginPatterns());
        config.setAllowedHeaders(corsProperties.getAllowedHeaders());
        config.setExposedHeaders(corsProperties.getExposedHeaders());
        config.setAllowedMethods(corsProperties.getAllowedMethods()
                .stream()
                .map(Enum::name)
                .collect(toList()));
        config.setAllowCredentials(corsProperties.isAllowCredentials());
        config.setMaxAge(corsProperties.getMaxAge());

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
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
