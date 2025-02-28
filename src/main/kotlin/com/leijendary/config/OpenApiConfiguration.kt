package com.leijendary.config

import io.swagger.v3.core.jackson.ModelResolver
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.security.OAuthFlow
import io.swagger.v3.oas.models.security.OAuthFlows
import io.swagger.v3.oas.models.security.Scopes
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.models.GroupedOpenApi
import org.springdoc.core.properties.SpringDocConfigProperties
import org.springdoc.core.properties.SpringDocConfigProperties.GroupConfig
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.time.temporal.ChronoUnit.SECONDS
import java.util.stream.Collectors

@Configuration(proxyBeanMethods = false)
class OpenApiConfiguration {
    init {
        ModelResolver.enumsAsRef = true
        replaceLocalDateTime()
    }

    @Bean
    fun groupedOpenApis(
        springDocConfigProperties: SpringDocConfigProperties,
        serverProperties: ServerProperties
    ): List<GroupedOpenApi> {
        return springDocConfigProperties.groupConfigs.map { groupConfig ->
            GroupedOpenApi.builder()
                .group(groupConfig.group)
                .pathsToMatch(*groupConfig.pathsToMatch.toTypedArray())
                .addOpenApiCustomizer(PathPrefixOpenApiCustomizer(groupConfig))
                .addOpenApiCustomizer(
                    CopyOpenApiCustomizer(serverProperties, groupConfig, springDocConfigProperties.openApi)
                )
                .build()
        }
    }

    private fun replaceLocalDateTime() {
        val schema = Schema<LocalDateTime>()
            .type("string")
            .format("yyyyMMdd'T'HH:mm:ss")
            .example(LocalDateTime.now().truncatedTo(SECONDS).format(ISO_LOCAL_DATE_TIME))

        SpringDocUtils.getConfig().replaceWithSchema(LocalDateTime::class.java, schema)
    }
}

class PathPrefixOpenApiCustomizer(private val groupConfig: GroupConfig) : OpenApiCustomizer {
    override fun customise(openApi: OpenAPI) {
        val url = groupConfig.openApi.servers.firstOrNull()?.url ?: return
        val paths = Paths()
        openApi.paths.forEach { path ->
            val key = path.key.replaceFirst(url, "")
            paths[key] = path.value
        }
        openApi.paths = paths
    }
}

class CopyOpenApiCustomizer(
    private val serverProperties: ServerProperties,
    private val groupConfig: GroupConfig,
    private val openApi: OpenAPI
) : OpenApiCustomizer {
    override fun customise(customizer: OpenAPI) {
        customizer.info = info()
        customizer.servers = servers()
        customizer.components.securitySchemes = securitySchemes(customizer.paths)
    }

    private fun info() = groupConfig.openApi.info.apply {
        contact = openApi.info.contact
        extensions = openApi.info.extensions
    }

    private fun servers(): List<Server> {
        val servers = openApi.servers.map {
            Server().apply {
                url = UriComponentsBuilder.fromUriString(it.url)
                    .path(serverProperties.servlet.contextPath)
                    .toUriString()
                description = it.description
            }
        }
        val path = groupConfig.openApi.servers.firstOrNull()?.url ?: return servers

        servers.forEach {
            it.url = UriComponentsBuilder.fromUriString(it.url)
                .path(path)
                .toUriString()
        }

        return servers
    }

    private fun securitySchemes(paths: Paths): Map<String, SecurityScheme> {
        val allScopes = paths.values.stream()
            .flatMap { it.readOperations().stream() }
            .filter { it.security !== null }
            .flatMap { it.security.stream() }
            .flatMap { it.entries.stream() }
            .collect(Collectors.toMap({ it.key }, { it.value.toSet() }, { existing, new -> existing + new }))

        return openApi.components.securitySchemes.mapValues {
            val schemeScopes = allScopes.getValue(it.key)

            SecurityScheme().apply {
                type = it.value.type
                flows = OAuthFlows().apply {
                    implicit = oAuthFlow(it.value.flows.implicit, schemeScopes)
                }
            }
        }
    }

    private fun oAuthFlow(oAuthFlow: OAuthFlow, schemeScopes: Set<String>) = OAuthFlow().apply {
        authorizationUrl = oAuthFlow.authorizationUrl
        scopes = Scopes().apply {
            oAuthFlow.scopes
                .filter { it.key in schemeScopes }
                .run(::putAll)
        }
    }
}
