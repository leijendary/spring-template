FROM maven:3-openjdk-11-slim as build
WORKDIR /workspace/app
ARG MAVEN_REPOSITORY=".m2/repository"
ENV MAVEN_OPTS="-Dmaven.repo.local=$MAVEN_REPOSITORY -Djava.awt.headless=true"
COPY pom.xml .
COPY src src
RUN --mount=type=cache,target=$MAVEN_REPOSITORY mvn package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM adoptopenjdk/openjdk11-openj9:alpine
VOLUME /app
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
