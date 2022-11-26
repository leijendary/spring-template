package com.leijendary.spring.template.core.filter

import com.leijendary.spring.template.core.config.properties.AuthProperties
import com.leijendary.spring.template.core.security.SecurityAuthentication
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse


@Component
@AutoConfigureBefore(BasicAuthenticationFilter::class)
class SecurityContextFilter(authProperties: AuthProperties) : OncePerRequestFilter() {
    private val anonymousUser = authProperties.anonymousUser.principal

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authentication = SecurityAuthentication(request, anonymousUser)

        SecurityContextHolder.getContext().authentication = authentication;

        filterChain.doFilter(request, response)
    }
}