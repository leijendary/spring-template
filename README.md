# Spring Boot Template for Microservices

- This template is intended for the microservice architecture
- Kafka is included in this template
- Sample classes are included
- **This template uses annotation based routing**
- **Intended for personal use only as this does not include complete features like JHipster**

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

### To run tests in native mode:

`./gradlew nativeTest`

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

Given the following specifications in a kubernetes ran locally (with autoscaling, of course):

- **Memory**: 512MiB
- **CPU**: 500m

```
          /\      |‾‾| /‾‾/   /‾‾/   
     /\  /  \     |  |/  /   /  /    
    /  \/    \    |     (   /   ‾‾\  
   /          \   |  |\  \ |  (‾)  | 
  / __________ \  |__| \__\ \_____/ .io

  execution: local
     script: k6/sample-admin.js
     output: -

  scenarios: (100.00%) 1 scenario, 500 max VUs, 6m0s max duration (incl. graceful stop):
           * default: Up to 500 looping VUs for 5m30s over 2 stages (gracefulRampDown: 30s, gracefulStop: 30s)


     █ sample admin CRUD operations

       ✓ create status is 201
       ✓ get status is 200
       ✓ update status is 200
       ✓ delete status is 204

     checks.........................: 100.00% ✓ 615448      ✗ 0     
     data_received..................: 277 MB  835 kB/s
     data_sent......................: 200 MB  602 kB/s
     group_duration.................: avg=1.02s    min=11.97ms med=451.16ms max=16.01s   p(90)=2.97s    p(95)=5.73s
     http_req_blocked...............: avg=6µs      min=0s      med=1µs      max=8.28ms   p(90)=3µs      p(95)=4µs  
     http_req_connecting............: avg=4.02µs   min=0s      med=0s       max=5.71ms   p(90)=0s       p(95)=0s   
     http_req_duration..............: avg=256.36ms min=1.56ms  med=96.49ms  max=10.94s   p(90)=394.07ms p(95)=1.52s
       { expected_response:true }...: avg=256.36ms min=1.56ms  med=96.49ms  max=10.94s   p(90)=394.07ms p(95)=1.52s
     http_req_failed................: 0.00%   ✓ 0           ✗ 615448
     http_req_receiving.............: avg=161µs    min=3µs     med=27µs     max=211.42ms p(90)=232µs    p(95)=492µs
     http_req_sending...............: avg=9.71µs   min=1µs     med=7µs      max=5.37ms   p(90)=16µs     p(95)=23µs 
     http_req_tls_handshaking.......: avg=0s       min=0s      med=0s       max=0s       p(90)=0s       p(95)=0s   
     http_req_waiting...............: avg=256.19ms min=1.53ms  med=96.34ms  max=10.94s   p(90)=393.83ms p(95)=1.52s
     http_reqs......................: 615448  1853.510163/s
     iteration_duration.............: avg=1.02s    min=12.02ms med=451.17ms max=16.01s   p(90)=2.97s    p(95)=5.73s
     iterations.....................: 153862  463.377541/s
     vus............................: 8       min=8         max=500 
     vus_max........................: 500     min=500       max=500 


running (5m32.0s), 000/500 VUs, 153862 complete and 0 interrupted iterations
default ✓ [======================================] 000/500 VUs  5m30s
```
