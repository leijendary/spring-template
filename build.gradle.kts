import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("kapt") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"
}

group = "com.leijendary.spring.boot"
version = "2.0.0"
description = "Spring Boot Template for the Microservice Architecture or general purpose"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

kapt {
    arguments {
        arg("mapstruct.defaultComponentModel", "spring")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-jose:5.6.1")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer:3.1.0")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka:3.2.1")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-streams:3.2.1")
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
    implementation("org.springframework.retry:spring-retry:1.3.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")
    implementation("org.liquibase:liquibase-core:4.7.1")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.1")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.6")
    implementation("org.springdoc:springdoc-openapi-security:1.6.5")
    implementation("org.mapstruct:mapstruct:1.4.2.Final")
    implementation("com.github.ben-manes.caffeine:caffeine:3.0.5")
    kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2:2.1.210")
    runtimeOnly("org.postgresql:postgresql:42.3.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test:5.6.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget = "11"
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    filesMatching("application.yaml") {
        expand(project.properties)
    }
}