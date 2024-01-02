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
- SpringDoc OpenAPI
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

     checks.........................: 100.00% ✓ 198530     ✗ 0     
     data_received..................: 132 MB  412 kB/s
     data_sent......................: 116 MB  360 kB/s
     http_req_blocked...............: avg=7.84µs   min=0s     med=1µs      max=19.31ms  p(90)=3µs      p(95)=4µs   
     http_req_connecting............: avg=5.33µs   min=0s     med=0s       max=19.28ms  p(90)=0s       p(95)=0s    
     http_req_duration..............: avg=458.66ms min=3.37ms med=336.09ms max=5.42s    p(90)=922.29ms p(95)=1.15s 
       { expected_response:true }...: avg=458.66ms min=3.37ms med=336.09ms max=5.42s    p(90)=922.29ms p(95)=1.15s 
     http_req_failed................: 0.00%   ✓ 0          ✗ 198530
     http_req_receiving.............: avg=298.97µs min=6µs    med=42µs     max=233.51ms p(90)=526µs    p(95)=1.09ms
     http_req_sending...............: avg=11.78µs  min=2µs    med=10µs     max=4.12ms   p(90)=18µs     p(95)=25µs  
     http_req_tls_handshaking.......: avg=0s       min=0s     med=0s       max=0s       p(90)=0s       p(95)=0s    
     http_req_waiting...............: avg=458.35ms min=3.33ms med=335.74ms max=5.42s    p(90)=921.95ms p(95)=1.15s 
     http_reqs......................: 198530  618.388325/s
     iteration_duration.............: avg=458.82ms min=3.56ms med=336.24ms max=5.42s    p(90)=922.42ms p(95)=1.15s 
     iterations.....................: 198530  618.388325/s
     vus............................: 39      min=5        max=499 
     vus_max........................: 500     min=500      max=500 


running (5m21.0s), 000/500 VUs, 198530 complete and 0 interrupted iterations
default ✓ [======================================] 000/500 VUs  5m20s

```
