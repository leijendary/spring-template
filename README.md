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
- Spring AI
- Spring Cache
- Spring Cloud AWS S3
- Spring Cloud OpenFeign
- Spring Configuration Processor
- Spring Data Elasticsearch
- Spring Data JDBC
- Spring Data Redis
- Spring Kafka
- Spring Native
- Spring Retry
- Spring Validation
- Spring Web
- AWS CDK
- Docker
- JUnit
- Kubernetes
- Liquibase
- Micrometer
- OpenAPI
- PostgreSQL
- Test Containers

# Spring Microservice Template

## Configuration

- Refresh gradle dependencies in IntelliJ
- Copy `.example.env` to `.env`
- Update the values in `.env`
- In IntelliJ, select `Current File`
- Select `Edit Configurations...`
- Click `Add new...`
- Select `Kotlin`
- Rename the configuration to `Application`
- Change "Use classpath of module..." to `server.main`
- Set `com.leijendary.ApplicationKt` as the main class
- Select the `EnvFile` tab
- Check `Enable EnvFile`
- Click `+`
- Locate the `.env` file in your local machine
- Click `OK`

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

`docker compose up`

# Branching Strategy

As you may have noticed, GitHub Actions are also included in this template repository. Each company has their own
different git workflow, and this is what I think is the fastest for most teams.

- `main` **deploys to dev**. This is ALWAYS updated. The sole purpose of this is for developer's testing.
- `release/*` **deploys to test**. Whatever is going to be placed here, means that this is a candidate for
  release. Only used by QA testers. This is where you start tagging.
- `tags` **deploys to sandbox AND prod**.
- Branches like `release/*` and `fix/*` should be very small and a PR should be opened to `main`. We still need to do
  code reviews somehow.
- Changes from `main` **may** be cherry-picked to `release/*`.
- `hotfix/*` merges to a `release/*` branch that is created from the latest production tag.
- When the `release/*` branch that contains the changes in `hotfix/*` is tagged and deployed to production, then the
  release branch will be merged back to `main`.

# Load Testing

This template uses [k6](https://grafana.com/docs/k6/latest/) to do the load testing.

## Running k6

`k6 run k6/sample/admin.js`

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
     script: k6/sample/admin.js
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
