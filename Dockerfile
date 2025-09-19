FROM ghcr.io/graalvm/graalvm-community:21 AS build
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
# Run GraalVM native compiler.
RUN --mount=type=cache,target=/root/.gradle ./gradlew nativeCompile -i -x test

# Run the application.
FROM alpine:3
RUN apk add gcompat
COPY --from=build /app/build/native/nativeCompile/app app
ENTRYPOINT ["./app"]
