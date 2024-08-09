package com.leijendary

import com.leijendary.config.properties.KafkaTopicProperties.KafkaTopic
import com.leijendary.model.IdentityModel
import com.leijendary.model.QueryRequest
import com.leijendary.validator.UniqueFieldsValidator
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP
import io.swagger.v3.oas.annotations.security.SecurityScheme
import liquibase.changelog.ChangeLogHistoryServiceFactory
import liquibase.database.LiquibaseTableNamesFactory
import liquibase.parser.SqlParserFactory
import liquibase.report.ShowSummaryGeneratorFactory
import liquibase.ui.LoggerUIService
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor
import org.apache.kafka.common.security.authenticator.AbstractLogin.DefaultLoginCallbackHandler
import org.apache.kafka.common.security.authenticator.DefaultLogin
import org.apache.kafka.common.security.authenticator.SaslClientAuthenticator
import org.apache.kafka.common.security.authenticator.SaslClientCallbackHandler
import org.apache.kafka.common.security.scram.ScramLoginModule
import org.apache.kafka.common.security.scram.internals.ScramFormatter
import org.apache.kafka.common.security.scram.internals.ScramSaslClient
import org.apache.kafka.common.security.scram.internals.ScramSaslClient.ScramSaslClientFactory
import org.bouncycastle.jcajce.provider.asymmetric.RSA.Mappings
import org.bouncycastle.jcajce.provider.asymmetric.rsa.KeyFactorySpi
import org.springframework.aot.hint.ExecutableMode.INVOKE
import org.springframework.aot.hint.MemberCategory.DECLARED_CLASSES
import org.springframework.aot.hint.MemberCategory.INVOKE_DECLARED_CONSTRUCTORS
import org.springframework.aot.hint.MemberCategory.INVOKE_DECLARED_METHODS
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.boot.SpringBootVersion
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.core.env.get
import org.springframework.data.web.PagedModel
import org.springframework.data.web.config.SpringDataJacksonConfiguration
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.kafka.retrytopic.RetryTopicConfigurer
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync
import javax.security.auth.spi.LoginModule
import javax.security.sasl.SaslClient

@SpringBootApplication(proxyBeanMethods = false)
@ImportRuntimeHints(ApplicationRuntimeHints::class)
@RegisterReflectionForBinding(
    value = [
        ChangeLogHistoryServiceFactory::class,
        CooperativeStickyAssignor::class,
        DefaultLogin::class,
        DefaultLoginCallbackHandler::class,
        KafkaTopic::class,
        KeyFactorySpi::class,
        LiquibaseTableNamesFactory::class,
        LoggerUIService::class,
        LoginModule::class,
        Mappings::class,
        PagedModel::class,
        QueryRequest::class,
        SaslClient::class,
        SaslClientAuthenticator::class,
        SaslClientCallbackHandler::class,
        ScramFormatter::class,
        ScramLoginModule::class,
        ScramSaslClient::class,
        ScramSaslClientFactory::class,
        ShowSummaryGeneratorFactory::class,
        SqlParserFactory::class,
        UniqueFieldsValidator::class,
    ]
)
@SecurityScheme(name = AUTHORIZATION, type = HTTP, `in` = HEADER, scheme = "bearer", bearerFormat = "JWT")
@EnableAsync
@EnableFeignClients
@EnableRetry
class Application

class ApplicationRuntimeHints : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        val memberCategories = arrayOf(INVOKE_DECLARED_CONSTRUCTORS, INVOKE_DECLARED_METHODS, DECLARED_CLASSES)
        // Classes to be registered for reflection
        val reflections = arrayOf(
            Class.forName("${RetryTopicConfigurer::class.qualifiedName}\$LoggingDltListenerHandlerMethod"),
            Class.forName("${SpringDataJacksonConfiguration::class.qualifiedName}\$PageModule\$PageModelConverter"),
            Class.forName("${SpringDataJacksonConfiguration::class.qualifiedName}\$PageModule\$PlainPageSerializationWarning")
        )
        // Paths to be registered as resources
        val resources = arrayOf(
            "messages/*",
            "io/awspring/cloud/core/SpringCloudClientConfiguration.properties",
            "io/awspring/cloud/s3/S3ObjectContentTypeResolver.properties"
        )
        // Classes to be registered for serialization
        val serializations = arrayOf(IdentityModel::class.java)

        // Reflection
        reflections.forEach { type ->
            hints.reflection().registerType(type) {
                it.withMembers(*memberCategories).withConstructor(emptyList(), INVOKE)
            }
        }

        // Resources
        resources.forEach(hints.resources()::registerPattern)

        // Serialization
        serializations.forEach(hints.serialization()::registerType)
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args) {
        setBanner { environment, _, out ->
            val name = environment["openApi.info.title"]
            val version = environment["openApi.info.version"]
            val springVersion = SpringBootVersion.getVersion()

            out.print("Running $name v$version on Spring Boot v$springVersion")
        }
    }
}
