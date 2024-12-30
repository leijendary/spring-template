import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

val openApiTasks = file("$rootDir/src/main/resources/specs").listFiles().map {
    val name = it.name.replace(".yaml", "")

    tasks.register(name, GenerateTask::class.java) {
        group = "openapi"
        description = "Generate models from the OpenAPI specifications of ${it.name}"
        generatorName.set("kotlin-spring")
        inputSpec.set(it.path)
        outputDir.set("$rootDir/build/generated")
        modelPackage.set("$name.model")
        generateModelDocumentation.set(false)
        generateModelTests.set(false)
        additionalProperties.set(mapOf("removeEnumValuePrefix" to "false"))
        globalProperties.set(mapOf("models" to ""))
        configOptions.set(
            mapOf(
                "documentationProvider" to "none",
                "enumPropertyNaming" to "UPPERCASE",
                "openApiNullable" to "false",
                "useBeanValidation" to "false",
                "useSpringBoot3" to "true"
            )
        )
    }
}

plugins {
    val kotlinVersion = "2.0.21"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.10.4"
    id("org.openapi.generator") version "7.10.0"
}

group = "com.leijendary"
description = "Spring boot template for the microservices architecture."
version = "0.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

kotlin {
    compilerOptions {
        apiVersion.set(KotlinVersion.KOTLIN_2_2)
        freeCompilerArgs.addAll("-Xjsr305=strict")
        languageVersion.set(KotlinVersion.KOTLIN_2_2)
        jvmTarget.set(JvmTarget.JVM_22)
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
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))

    // Spring Boot Starter
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")

    // Spring Cloud Starter
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2024.0.0-SNAPSHOT"))
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // Spring Kafka
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    // Spring Retry
    implementation("org.springframework.retry:spring-retry")

    // AI
    implementation(platform("org.springframework.ai:spring-ai-bom:1.0.0-SNAPSHOT"))
    implementation("org.springframework.ai:spring-ai-ollama-spring-boot-starter")
    implementation("org.springframework.ai:spring-ai-pgvector-store-spring-boot-starter")
    testImplementation("org.springframework.ai:spring-ai-spring-boot-testcontainers")

    // AWS
    implementation(platform("software.amazon.awssdk:bom:2.29.19"))
    implementation("software.amazon.awssdk:cloudfront")

    // AWS Cloud
    implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:3.2.1"))
    implementation("io.awspring.cloud:spring-cloud-aws-starter-metrics")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-s3")

    // Database
    implementation("org.postgresql:postgresql")
    implementation("org.liquibase:liquibase-core")

    // Devtools
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Observability and Metrics
    implementation("io.github.openfeign:feign-micrometer")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")
    implementation("net.ttddyy.observation:datasource-micrometer-spring-boot:1.0.6")

    // OpenAPI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    // Test
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    // Test Container
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:elasticsearch")
    testImplementation("org.testcontainers:kafka")
    testImplementation("org.testcontainers:ollama")
    testImplementation("org.testcontainers:postgresql")

    // Utility
    implementation("io.github.thibaultmeyer:cuid:2.0.3")
}

sourceSets {
    main {
        kotlin {
            srcDir("$rootDir/build/generated/src/main/kotlin")
        }
    }
}

graalvmNative {
    binaries {
        named("main") {
            imageName = "app"
        }
    }
}

tasks {
    compileKotlin {
        openApiTasks.let(dependsOn::addAll)
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
