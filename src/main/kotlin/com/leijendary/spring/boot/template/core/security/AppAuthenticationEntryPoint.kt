package com.leijendary.spring.boot.template.core.security

import com.leijendary.spring.boot.template.core.config.properties.AuthProperties
import com.leijendary.spring.boot.template.core.data.ErrorResponse
import com.leijendary.spring.boot.template.core.util.AnyUtil.toJson
import com.leijendary.spring.boot.template.core.util.RequestContext.locale
import org.apache.commons.lang3.StringUtils.isNotBlank
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AppAuthenticationEntryPoint(
    private val authProperties: AuthProperties,
    private val messageSource: MessageSource
) : AuthenticationEntryPoint {
    companion object {
        private fun computeWwwAuthenticateHeaderValue(parameters: Map<String, Any>): String {
            val wwwAuthenticate = StringBuilder("Bearer")

            if (parameters.isEmpty()) {
                return wwwAuthenticate.toString()
            }

            wwwAuthenticate.append(" ")

            var i = 0
            val iterator = parameters.entries.iterator()

            while (iterator.hasNext()) {
                val (key, value) = iterator.next()
                wwwAuthenticate.append(key).append("=\"").append(value).append("\"")

                if (i != parameters.size - 1) {
                    wwwAuthenticate.append(", ")
                }

                ++i
            }

            return wwwAuthenticate.toString()
        }
    }

    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest, response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        var code = "access.unauthorized"

        if (exception is OAuth2AuthenticationException) {
            val oAuth2Error: OAuth2Error = exception.error
            val description: String? = oAuth2Error.description

            if (isNotBlank(description)) {
                if (description!!.contains("expired")) {
                    code = "access.expired"
                } else if (description.contains("Invalid signature")) {
                    code = "access.invalid"
                }
            }
        }

        val errorResponse: ErrorResponse = buildResponse(code)
        val meta: Map<String, Any> = errorResponse.meta
        val wwwAuthenticate = computeWwwAuthenticateHeaderValue(meta)
        val json: String = errorResponse.toJson() ?: "";

        response.addHeader("WWW-Authenticate", wwwAuthenticate)
        response.status = UNAUTHORIZED.value()
        response.contentType = APPLICATION_JSON_VALUE
        response.writer.write(json)
        response.flushBuffer()
    }

    private fun buildResponse(code: String): ErrorResponse {
        val message = messageSource.getMessage(code, arrayOfNulls(0), locale)
        val realm: String = authProperties.realm

        return ErrorResponse.builder()
            .addError(mutableListOf("header", "authorization"), code, message)
            .meta("realm", realm)
            .status(UNAUTHORIZED)
            .build()
    }
}