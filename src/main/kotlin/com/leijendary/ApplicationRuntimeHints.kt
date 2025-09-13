package com.leijendary

import com.leijendary.config.properties.KafkaTopicProperties.KafkaTopic
import com.leijendary.model.IdentityModel
import com.leijendary.model.QueryRequest
import com.leijendary.validator.UniqueFieldsValidator
import feign.Client
import feign.micrometer.MicrometerObservationCapability
import liquibase.changelog.ChangeLogHistoryServiceFactory
import liquibase.changelog.FastCheckService
import liquibase.changelog.visitor.ValidatingVisitorGeneratorFactory
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
import org.springframework.aot.hint.MemberCategory.*
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.data.web.PagedModel
import org.springframework.data.web.config.SpringDataJacksonConfiguration.PageModule
import org.springframework.kafka.retrytopic.RetryTopicConfigurer
import javax.security.auth.spi.LoginModule
import javax.security.sasl.SaslClient

class ApplicationRuntimeHints : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        val memberCategories = arrayOf(INVOKE_DECLARED_CONSTRUCTORS, INVOKE_DECLARED_METHODS, DECLARED_CLASSES)
        // Classes to be registered for reflection
        val types = arrayOf(
            ChangeLogHistoryServiceFactory::class.java,
            CooperativeStickyAssignor::class.java,
            DefaultLogin::class.java,
            DefaultLoginCallbackHandler::class.java,
            FastCheckService::class.java,
            IdentityModel::class.java,
            KafkaTopic::class.java,
            KeyFactorySpi::class.java,
            LiquibaseTableNamesFactory::class.java,
            LoggerUIService::class.java,
            LoginModule::class.java,
            Mappings::class.java,
            PageModule::class.java,
            PagedModel::class.java,
            QueryRequest::class.java,
            SaslClient::class.java,
            SaslClientAuthenticator::class.java,
            SaslClientCallbackHandler::class.java,
            ScramFormatter::class.java,
            ScramLoginModule::class.java,
            ScramSaslClient::class.java,
            ScramSaslClientFactory::class.java,
            ShowSummaryGeneratorFactory::class.java,
            SqlParserFactory::class.java,
            UniqueFieldsValidator::class.java,
            ValidatingVisitorGeneratorFactory::class.java,
            // Inaccessible classes
            Class.forName($$"$${RetryTopicConfigurer::class.qualifiedName}$LoggingDltListenerHandlerMethod"),
        )
        // Methods to be registered for reflection
        val methods = arrayOf(MicrometerObservationCapability::class.java.getMethod("enrich", Client::class.java))
        // Paths to be registered as resources
        val resources = arrayOf(
            "messages/*",
            "prompts/*",
            "static/*",
            "io/awspring/cloud/core/SpringCloudClientConfiguration.properties",
            "io/awspring/cloud/s3/S3ObjectContentTypeResolver.properties"
        )

        // Type reflection
        types.forEach { type ->
            hints.reflection().registerType(type) {
                it.withMembers(*memberCategories).withConstructor(emptyList(), INVOKE)
            }
        }

        // Method reflection
        methods.forEach { hints.reflection().registerMethod(it, INVOKE) }

        // Resources
        resources.forEach(hints.resources()::registerPattern)
    }
}
