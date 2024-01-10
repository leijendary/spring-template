FROM ghcr.io/graalvm/graalvm-community:21 as build

# Download the gradle distribution
COPY gradlew .
COPY gradle/ gradle/
RUN ./gradlew --version

# Download dependencies
COPY settings.gradle.kts .
COPY build.gradle.kts .
RUN ./gradlew dependencies

# Add source code
COPY src/ src/

# Run GraalVM native compiler
RUN ./gradlew nativeCompile -x test

FROM scratch
COPY --from=build /build/native/nativeCompile/* app
ENTRYPOINT ./application
