# Spring Boot Template for Microservices

- This template is intended for the microservice architecture
- Kafka is included in this template
- Sample classes are included
- **This template uses annotation based routing**
- **Intended for personal use only as this does not include complete features like JHipster**
- Although provided, spring native is not the priority of this template because of the following reasons:
    - Very slow to compile
    - `arm64` is not supported by GitHub by default

# Technologies Used:

- Kotlin
- Spring Boot 3
- Spring Actuator
- Spring Cache
- Spring Cloud AWS
- Spring Cloud AWS S3
- Spring Cloud LoadBalancer
- Spring Cloud OpenFeign
- Spring Configuration Processor
- Spring Data Elasticsearch
- Spring Data Redis
- Spring JDBC
- Spring Kafka
- Spring Native
- Spring Retry
- Spring Validation
- Spring Web
- AWS CDK
- Caffeine
- Docker
- JUnit
- Kubernetes
- Liquibase
- MapStruct
- Micrometer Tracing
- OpenAPI
- OpenTelemetry
- PostgreSQL
- Prometheus
- Test Containers

# Spring Microservice Template

### To run the code:

`./gradlew bootRun`

### To run tests:

`./gradlew test`

### To build a JAR file:

`./gradlew build`

### To compile native code:

`./gradlew nativeCompile`

### To build native docker image:

`./gradlew bootBuildImage`

# Load Testing

This template uses [k6](https://grafana.com/docs/k6/latest/) to do the load testing. k6 is by far the best load testing
framework for developer experience.

### Result

Given the following specifications in a kubernetes ran locally:

- **Memory**: 1G
- **CPU**: 0.5

```
          /\      |‾‾| /‾‾/   /‾‾/   
     /\  /  \     |  |/  /   /  /    
    /  \/    \    |     (   /   ‾‾\  
   /          \   |  |\  \ |  (‾)  | 
  / __________ \  |__| \__\ \_____/ .io

  execution: local
     script: k6/script.js
     output: -

  scenarios: (100.00%) 1 scenario, 500 max VUs, 5m50s max duration (incl. graceful stop):
           * default: Up to 500 looping VUs for 5m20s over 2 stages (gracefulRampDown: 30s, gracefulStop: 30s)


     ✓ create status is 201

     checks.........................: 100.00% ✓ 227826     ✗ 0     
     data_received..................: 152 MB  472 kB/s
     data_sent......................: 132 MB  413 kB/s
     http_req_blocked...............: avg=8.63µs   min=0s     med=1µs      max=31.27ms p(90)=3µs      p(95)=4µs  
     http_req_connecting............: avg=5.94µs   min=0s     med=0s       max=31.23ms p(90)=0s       p(95)=0s   
     http_req_duration..............: avg=399.38ms min=2.18ms med=263.14ms max=13.69s  p(90)=761.07ms p(95)=1.08s
       { expected_response:true }...: avg=399.38ms min=2.18ms med=263.14ms max=13.69s  p(90)=761.07ms p(95)=1.08s
     http_req_failed................: 0.00%   ✓ 0          ✗ 227826
     http_req_receiving.............: avg=168.04µs min=6µs    med=32µs     max=53.18ms p(90)=286µs    p(95)=629µs
     http_req_sending...............: avg=13.05µs  min=2µs    med=9µs      max=8.89ms  p(90)=20µs     p(95)=26µs 
     http_req_tls_handshaking.......: avg=0s       min=0s     med=0s       max=0s      p(90)=0s       p(95)=0s   
     http_req_waiting...............: avg=399.2ms  min=2.15ms med=262.98ms max=13.69s  p(90)=760.87ms p(95)=1.08s
     http_reqs......................: 227826  710.071007/s
     iteration_duration.............: avg=399.56ms min=2.35ms med=263.31ms max=13.69s  p(90)=761.26ms p(95)=1.08s
     iterations.....................: 227826  710.071007/s
     vus............................: 499     min=5        max=499 
     vus_max........................: 500     min=500      max=500 


running (5m20.8s), 000/500 VUs, 227826 complete and 0 interrupted iterations
default ✓ [======================================] 000/500 VUs  5m20s
```
