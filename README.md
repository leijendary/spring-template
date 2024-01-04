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
     script: k6/sample-admin.js
     output: -

  scenarios: (100.00%) 1 scenario, 500 max VUs, 5m50s max duration (incl. graceful stop):
           * default: Up to 500 looping VUs for 5m20s over 2 stages (gracefulRampDown: 30s, gracefulStop: 30s)

     ✓ create status is 201
     ✓ get status is 200
     ✓ update status is 200
     ✓ update status is 204

     checks.........................: 100.00% ✓ 238776     ✗ 0     
     data_received..................: 126 MB  391 kB/s
     data_sent......................: 87 MB   270 kB/s
     http_req_blocked...............: avg=10.56µs  min=0s      med=2µs      max=148.93ms p(90)=4µs      p(95)=5µs  
     http_req_connecting............: avg=5.79µs   min=0s      med=0s       max=32.45ms  p(90)=0s       p(95)=0s   
     http_req_duration..............: avg=383.13ms min=1.96ms  med=228.26ms max=8.19s    p(90)=880.35ms p(95)=1.21s
       { expected_response:true }...: avg=383.13ms min=1.96ms  med=228.26ms max=8.19s    p(90)=880.35ms p(95)=1.21s
     http_req_failed................: 0.00%   ✓ 0          ✗ 238776
     http_req_receiving.............: avg=219.79µs min=4µs     med=36µs     max=298.12ms p(90)=320µs    p(95)=802µs
     http_req_sending...............: avg=16.96µs  min=2µs     med=9µs      max=164.32ms p(90)=21µs     p(95)=29µs 
     http_req_tls_handshaking.......: avg=0s       min=0s      med=0s       max=0s       p(90)=0s       p(95)=0s   
     http_req_waiting...............: avg=382.89ms min=1.92ms  med=228.05ms max=8.19s    p(90)=880.17ms p(95)=1.21s
     http_reqs......................: 238776  740.289539/s
     iteration_duration.............: avg=1.53s    min=13.05ms med=999.13ms max=13.54s   p(90)=3.55s    p(95)=4.49s
     iterations.....................: 59694   185.072385/s
     vus............................: 179     min=5        max=499 
     vus_max........................: 500     min=500      max=500 


running (5m22.5s), 000/500 VUs, 59694 complete and 0 interrupted iterations
default ✓ [======================================] 000/500 VUs  5m20s
```
