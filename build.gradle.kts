import org.gradle.api.file.DuplicatesStrategy.INCLUDE

plugins {
    id("org.springframework.boot") version "3.0.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.7.21"
    id("org.graalvm.buildtools.native") version "0.9.18"
    id("org.barfuin.gradle.jacocolog") version "2.0.0"
    kotlin("jvm") version "1.7.21"
    kotlin("kapt") version "1.7.21"
    kotlin("plugin.spring") version "1.7.21"
    kotlin("plugin.jpa") version "1.7.21"
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

springBoot {
    mainClass.set("com.leijendary.spring.template.Application")
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
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Spring Boot Starter
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Spring Retry
    implementation("org.springframework.retry:spring-retry:2.0.0")

    // Spring Cloud Starter
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer:3.1.5")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.5")

    // Spring Cloud Stream
    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-streams")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.apache.kafka:kafka-streams")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    // Jackson
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")

    // Cache
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.1")

    // AWS
    implementation("io.awspring.cloud:spring-cloud-aws-starter-s3")

    // Database
    implementation("org.liquibase:liquibase-core:4.17.2")
    implementation("org.hibernate:hibernate-core-jakarta:5.6.14.Final")
    runtimeOnly("org.postgresql:postgresql:42.5.0")

    // OpenAPI
    implementation("org.springdoc:springdoc-openapi-ui:1.6.13")

    // MapStruct
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")
    testImplementation("org.mapstruct:mapstruct-processor:1.5.3.Final")

    // Devtools
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
}

dependencyManagement {
    imports {
        mavenBom("io.awspring.cloud:spring-cloud-aws-dependencies:3.0.0-M3")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.0-RC2")
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
