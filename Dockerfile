FROM ghcr.io/graalvm/graalvm-community:21 as build

# Download the gradle distribution
COPY gradlew .
COPY gradle/ gradle/
RUN --mount=type=cache,target=/root/.gradle ./gradlew --version

# Download dependencies
COPY settings.gradle.kts .
COPY build.gradle.kts .
RUN --mount=type=cache,target=/root/.gradle ./gradlew dependencies

# Add source code
COPY src/ src/

# Run GraalVM native compiler
RUN ./gradlew nativeCompile -x test

# Run the application
FROM scratch
COPY --from=build /build/native/nativeCompile/* app
ENTRYPOINT ./application
