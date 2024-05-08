FROM ghcr.io/graalvm/graalvm-community:21 AS build
# Copy existing Gradle directory. This is usually from existing test cache.
COPY .gradle/ /root/.gradle/
# Download the Gradle distribution.
COPY gradlew .
COPY gradle/ gradle/
RUN --mount=type=cache,target=/root/.gradle ./gradlew --version
# Download dependencies.
COPY settings.gradle.kts .
COPY build.gradle.kts .
RUN --mount=type=cache,target=/root/.gradle ./gradlew dependencies
# Add source code.
COPY src/ src/
# Run GraalVM native compiler.
RUN --mount=type=cache,target=/root/.gradle ./gradlew nativeCompile -x test

# Run the application.
FROM alpine:3
RUN apk add gcompat
COPY --from=build /app/build/native/nativeCompile/app app
ENTRYPOINT ./app
