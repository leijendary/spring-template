FROM azul/zulu-openjdk-alpine:23 AS build
# Download the Gradle distribution.
COPY gradlew .
COPY gradle gradle
RUN --mount=type=cache,target=/root/.gradle ./gradlew --version
# Download dependencies.
COPY settings.gradle.kts .
COPY build.gradle.kts .
RUN --mount=type=cache,target=/root/.gradle ./gradlew dependencies
# Add source code.
COPY src src
# Build the application.
RUN --mount=type=cache,target=/root/.gradle ./gradlew bootJar -i -x test
RUN java -Djarmode=tools -jar build/libs/app.jar extract
RUN java -XX:ArchiveClassesAtExit=app/archive.jsa -Dspring.context.exit=onRefresh -jar app/app.jar || true

# Run the application.
FROM azul/zulu-openjdk-alpine:23-jre-headless
COPY --from=build app app
ENTRYPOINT ["java", "-XX:SharedArchiveFile=archive.jsa", "-Dspring.aot.enabled=true", "-jar", "app.jar"]
