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
- Spring Security
- Spring OAuth2 Resource Server
- Spring Security OAuth2 JOSE
- Spring Cloud Openfeign
- Spring Cloud Loadbalancer
- Spring Cache
- Spring Data Redis
- Spring Retry
- Spring Cloud Stream Binder Kafka
- Spring Cloud Stream Binder Kafka Streams
- Spring Cloud AWS
- Spring Configuration Processor
- Spring Autoconfigure Processor
- Spring Devtools
- PostgreSQL
- Liquibase
- Swagger
- MapStruct
- Caffeine
- Docker
- JUnit
- ElasticSearch

# Spring Microservice Template

### To run the code:

`./gradlew bootRun`

### To run tests:

`./gradlew test`

### To build a JAR file:

`./gradlew bootJar -x test`

### To generate a certificate:

`keytool -genkeypair -alias spring-boot -keyalg RSA -keysize 2048 -validity 3650 -keypass spring-boot -storetype PKCS12 -keystore keystore.p12`
