# syntax=docker/dockerfile:experimental
FROM maven:3-adoptopenjdk-11-openj9 as build
WORKDIR /workspace/app
ARG MAVEN_OPTS="-Dmaven.repo.local=.m2/repository -Djava.awt.headless=true"
ENV MAVEN_OPTS=${MAVEN_OPTS}
COPY pom.xml .
COPY src src
RUN --mount=type=cache,target=.m2/repository mvn package -DskipTests
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
    "com.leijendary.spring.boot.template.Application" \
]
