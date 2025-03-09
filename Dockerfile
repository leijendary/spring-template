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

# Run the application.
FROM azul/zulu-openjdk-alpine:23-jre-headless
COPY --from=build build/libs/app.jar .
ENTRYPOINT ["java", "-Dspring.aot.enabled=true", "-jar", "app.jar"]
