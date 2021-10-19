# syntax=docker/dockerfile:experimental
FROM maven:3-adoptopenjdk-11-openj9 as build
WORKDIR /workspace/app
COPY pom.xml .
COPY src src
RUN --mount=type=cache,target=.m2/repository mvn package -D skipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM adoptopenjdk/openjdk11-openj9:alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT [ \
    "java", \
    "-Xshareclasses", \
    "-Xquickstart", \
    "-cp", \
    "app:app/lib/*", \
    "com.leijendary.spring.microservicetemplate.Application" \
]
