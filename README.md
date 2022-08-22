# Spring Boot Template for Microservices

- This template is intended for the microservice architecture
- Kafka is included in this template
- Sample classes are included
- **This template uses annotation based routing**
- **Intended for personal use only as this does not include complete features like JHipster**

# Technologies Used:

- Kotlin
- Spring Configuration Processor
- Spring Actuator
- Spring Cache
- Spring Data Elasticsearch
- Spring Data JPA
- Spring Data Redis
- Spring Security
- Spring Web
- Spring Cloud AWS
- Spring Cloud Loadbalancer
- Spring Cloud OpenFeign
- Spring Cloud OpenTelemetry
- Spring Cloud Sleuth
- Spring Cloud Stream Binder Kafka
- Spring Cloud Stream Binder Kafka Streams
- Spring Retry
- Spring Devtools
- PostgreSQL
- Liquibase
- MapStruct
- Caffeine
- Docker
- JUnit
- OpenAPI
- Prometheus
- Kubernetes

# Spring Microservice Template

### To run the code:

`./gradlew bootRun`

### To run tests:

`./gradlew test`

### To build a JAR file:

`./gradlew build -x test`

### To generate a certificate:

`keytool -genkeypair -alias spring-boot -keyalg RSA -keysize 2048 -validity 3650 -keypass spring-boot -storetype PKCS12 -keystore keystore.p12`
