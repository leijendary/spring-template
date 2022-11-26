package com.leijendary.spring.template.core.config

import com.leijendary.spring.template.core.config.properties.AuthProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfiguration(private val authProperties: AuthProperties) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .headers()
            .and()
            .csrf().disable()
            .authorizeHttpRequests()
            .anyRequest().permitAll()
            .and()
            .anonymous { it.principal(authProperties.anonymousUser.principal) }
            .httpBasic().disable()
            .formLogin().disable()
            .logout().disable()
            .sessionManagement()
            .sessionCreationPolicy(STATELESS)
            .and()
            .exceptionHandling()
            .accessDeniedHandler { _, _, ex -> throw ex }
            .authenticationEntryPoint { _, _, ex -> throw ex }
            .and()
            .build()
    }
}