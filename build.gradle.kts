import org.gradle.api.file.DuplicatesStrategy.INCLUDE

plugins {
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.6.21"
    id("org.barfuin.gradle.jacocolog") version "2.0.0"
    kotlin("jvm") version "1.6.21"
    kotlin("kapt") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
}

group = "com.leijendary.spring"
version = "1.0.0"
description = "Spring Boot Template for the Microservice Architecture or general purpose"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    maven("https://repo.spring.io/snapshot")
    maven("https://repo.spring.io/milestone")
    mavenCentral()
}

kapt {
    arguments {
        arg("mapstruct.defaultComponentModel", "spring")
        arg("mapstruct.unmappedTargetPolicy", "IGNORE")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-configuration-processor:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-cache:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-security:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.3")
    implementation("org.springframework.cloud:spring-cloud-sleuth-otel-autoconfigure")
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer:3.1.3")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.3")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth:3.1.3") {
        configurations {
            all {
                exclude("org.springframework.cloud", "spring-cloud-sleuth-brave")
                exclude("io.zipkin.brave")
            }
        }
    }
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka:3.2.4")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-streams:3.2.4")
    implementation("org.springframework.retry:spring-retry:1.3.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.1")
    implementation("com.github.gavlyukovskiy:datasource-proxy-spring-boot-starter:1.8.0")
    implementation("io.opentelemetry:opentelemetry-extension-trace-propagators:1.17.0")
    implementation("io.opentelemetry:opentelemetry-exporter-jaeger:1.17.0")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp-common:1.17.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.liquibase:liquibase-core:4.15.0")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.10")
    implementation("org.mapstruct:mapstruct:1.5.2.Final")
    developmentOnly("org.springframework.boot:spring-boot-devtools:2.7.3")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus:1.9.3")
    runtimeOnly("org.postgresql:postgresql:42.4.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.3")
    kapt("org.mapstruct:mapstruct-processor:1.5.2.Final")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.3")
        mavenBom("org.springframework.cloud:spring-cloud-sleuth-otel-dependencies:1.1.0-M6")
    }
}

tasks.compileKotlin {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget = "17"
    }
}

tasks.bootJar {
    duplicatesStrategy = INCLUDE
}

tasks.jar {
    enabled = false
}

tasks.test {
    jvmArgs = listOf("-XX:+AllowRedefinitionToAddDeleteMethods")
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

tasks.processResources {
    filesMatching("application.yaml") {
        expand(project.properties)
    }
}
