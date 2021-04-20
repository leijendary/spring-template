package com.leijendary.spring.microservicetemplate.config;

import com.leijendary.spring.microservicetemplate.config.properties.AuthProperties;
import com.leijendary.spring.microservicetemplate.config.properties.CorsProperties;
import com.leijendary.spring.microservicetemplate.security.AppAuthenticationEntryPoint;
import com.leijendary.spring.microservicetemplate.security.AudienceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.stream.Collectors;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AppAuthenticationEntryPoint appAuthenticationEntryPoint;
    private final AuthProperties authProperties;
    private final CorsProperties corsProperties;
    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().permitAll()
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(appAuthenticationEntryPoint)
            .and()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
            .and()
                .cors()
                .configurationSource(corsConfigurationSource())
            .and()
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .oauth2ResourceServer()
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
                .collect(Collectors.toList()));
        config.setAllowCredentials(corsProperties.isAllowCredentials());
        config.setMaxAge(corsProperties.getMaxAge());

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    private JwtDecoder jwtDecoder() {
        final var audience = authProperties.getAudience();
        final var issuer = oAuth2ResourceServerProperties.getJwt().getIssuerUri();
        final var withAudience = new AudienceValidator(audience);
        final var withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        final var validator = new DelegatingOAuth2TokenValidator<>(withAudience, withIssuer);
        final var jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuer);
        jwtDecoder.setJwtValidator(validator);

        return jwtDecoder;
    }
}
