import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES

val openApiTasks = File("$rootDir/src/main/resources/specs").listFiles()?.map {
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
    val kotlinVersion = "2.0.20"

    id("org.springframework.boot") version "3.3.3"
    id("org.graalvm.buildtools.native") version "0.10.2"
    id("org.openapi.generator") version "7.7.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
}

group = "com.leijendary"
description = "Spring boot template for the microservices architecture."
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_22
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
}

dependencies {
    implementation(platform(BOM_COORDINATES))

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

    // Spring Cloud Starter
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2023.0.3"))
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // Spring Kafka
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    // Spring Retry
    implementation("org.springframework.retry:spring-retry")

    // AI
    implementation(platform("org.springframework.ai:spring-ai-bom:1.0.0-M1"))
    implementation("org.springframework.ai:spring-ai-ollama-spring-boot-starter")

    // AWS
    implementation(platform("software.amazon.awssdk:bom:2.26.22"))
    implementation("software.amazon.awssdk:cloudfront")

    // AWS Cloud
    implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:3.1.1"))
    implementation("io.awspring.cloud:spring-cloud-aws-starter")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-s3")

    // Database
    implementation("org.postgresql:postgresql")
    implementation("org.liquibase:liquibase-core")

    // Devtools
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Observability and Metrics
    implementation(platform("io.micrometer:micrometer-tracing-bom:1.3.2"))
    implementation("io.github.openfeign:feign-micrometer")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")
    implementation("net.ttddyy.observation:datasource-micrometer-spring-boot:1.0.5")

    // OpenAPI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    // Test
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")

    // Test Containers
    testImplementation(platform("org.testcontainers:testcontainers-bom:1.20.0"))
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:elasticsearch")
    testImplementation("org.testcontainers:kafka")
    testImplementation("org.testcontainers:ollama")
    testImplementation("org.testcontainers:postgresql")
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
