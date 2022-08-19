# Spring Boot Template for Microservices

- This template is intended for the microservice architecture
- Kafka is included in this template
- Sample classes are included
- **This template uses annotation based routing**
- **Intended for personal use only as this does not include complete features like JHipster**

# Technologies Used:

- Spring Actuator
- Spring Data JPA
- Spring Web
- Spring Cloud Openfeign
- Spring Cloud Loadbalancer
- Spring Cache
- Spring Data Redis
- Spring Retry
- Spring Cloud Stream Binder Kafka
- Spring Cloud Stream Binder Kafka Streams
- Spring Cloud AWS
- Spring Cloud Sleuth
- Spring Cloud OpenTelemetry
- Spring Configuration Processor
- Spring Autoconfigure Processor
- Spring Devtools
- Spring Validation
- PostgreSQL
- Liquibase
- MapStruct
- Caffeine
- Docker
- JUnit
- Elasticsearch
- Kubernetes
- OpenAPI
- Prometheus
- OpenTelemetry

# Spring Microservice Template

### To run the code:

`./gradlew bootRun`

### To run tests:

`./gradlew test`

### To build a JAR file:

`./gradlew build -x test`

### To generate a certificate:

`keytool -genkeypair -alias spring-boot -keyalg RSA -keysize 2048 -validity 3650 -keypass spring-boot -storetype PKCS12 -keystore keystore.p12`
