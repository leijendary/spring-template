package com.leijendary.spring.template.core.config

import com.leijendary.spring.template.core.config.properties.AuthProperties
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfiguration(private val authProperties: AuthProperties) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .headers()
            .and()
            .csrf().disable()
            .authorizeRequests()
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