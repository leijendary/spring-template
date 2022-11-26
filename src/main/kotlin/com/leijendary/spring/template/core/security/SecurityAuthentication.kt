package com.leijendary.spring.template.core.security

import com.leijendary.spring.template.core.util.HEADER_SCOPE
import com.leijendary.spring.template.core.util.HEADER_USER_ID
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import jakarta.servlet.http.HttpServletRequest

class SecurityAuthentication(request: HttpServletRequest, anonymousUser: String) : Authentication {
    private val userId = request.getHeader(HEADER_USER_ID)
    private val authorities = request
        .getHeader(HEADER_SCOPE)
        ?.split(" ")
        ?.map { GrantedAuthority { it } }
        ?: listOf()
    private val principal = userId ?: anonymousUser
    private var isAuthenticated = true

    override fun getName(): String = principal

    override fun getAuthorities(): List<GrantedAuthority> = authorities

    override fun getCredentials(): Any? = userId

    override fun getDetails(): Any? = userId

    override fun getPrincipal(): Any = principal

    override fun isAuthenticated(): Boolean = isAuthenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated
    }
}