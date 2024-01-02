import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

val openApiTasks = File("$rootDir/src/main/resources/specs").listFiles()?.map {
    val name = it.name.replace(".yaml", "")

    tasks.register("openApiGenerate-$name", GenerateTask::class.java) {
        generatorName.set("kotlin-spring")
        inputSpec.set(it.path)
        outputDir.set("$rootDir/build/generated")
        modelPackage.set("$name.model")
        generateModelDocumentation.set(false)
        generateModelTests.set(false)
        additionalProperties.set(mapOf("removeEnumValuePrefix" to "false"))
        configOptions.set(
            mapOf(
                "documentationProvider" to "none",
                "enumPropertyNaming" to "UPPERCASE",
                "openApiNullable" to "false",
                "useBeanValidation" to "false",
                "useSpringBoot3" to "true"
            )
        )
        globalProperties.set(mapOf("models" to ""))
    }
}

plugins {
    val kotlinVersion = "2.0.0-Beta2"

    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.graalvm.buildtools.native") version "0.9.28"
    id("org.openapi.generator") version "7.0.1"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
}

group = "com.leijendary"
description = "Spring boot template for the microservices architecture."
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        apiVersion.set(KotlinVersion.KOTLIN_2_1)
        freeCompilerArgs.addAll("-Xjsr305=strict")
        languageVersion.set(KotlinVersion.KOTLIN_2_1)
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    testImplementation {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    // Kotlin
    implementation(kotlin("reflect"))

    // Spring Boot Starter
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Spring Cloud Starter
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // Spring Kafka
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    // Spring Retry
    implementation("org.springframework.retry:spring-retry")

    // AWS
    implementation("io.awspring.cloud:spring-cloud-aws-starter")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-s3")
    implementation("software.amazon.awssdk:cloudfront")

    // Cache
    implementation("com.github.ben-manes.caffeine:caffeine")

    // Database
    implementation("org.postgresql:postgresql")
    implementation("org.liquibase:liquibase-core")

    // Devtools
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // OpenAPI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // Test
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("org.mockito:mockito-inline:5.2.0")

    // Test Containers
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:elasticsearch")
    testImplementation("org.testcontainers:kafka")
    testImplementation("org.testcontainers:postgresql")

    // Tracing
    implementation("com.github.loki4j:loki-logback-appender:1.4.2")
    implementation("io.github.openfeign:feign-micrometer")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("net.ttddyy.observation:datasource-micrometer-spring-boot:1.0.3")
}

dependencyManagement {
    imports {
        mavenBom("io.awspring.cloud:spring-cloud-aws-dependencies:3.1.0")
        mavenBom("io.micrometer:micrometer-tracing-bom:1.2.1")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.0")
        mavenBom("org.testcontainers:testcontainers-bom:1.19.3")
        mavenBom("software.amazon.awssdk:bom:2.22.7")
    }
}

sourceSets {
    main {
        kotlin {
            srcDir("$rootDir/build/generated/src/main/kotlin")
        }
    }
}

tasks {
    compileKotlin {
        openApiTasks?.let(dependsOn::addAll)
    }

    processResources {
        filesMatching("application.yaml") {
            expand(project.properties)
        }
    }

    test {
        useJUnitPlatform()
    }
}
