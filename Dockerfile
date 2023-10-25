FROM ghcr.io/graalvm/graalvm-community:21 as build
WORKDIR /workspace
COPY gradlew .
COPY gradle gradle
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY src src
RUN --mount=type=cache,target=/root/.gradle ./gradlew nativeCompile --no-daemon -i -x test

FROM alpine:3
RUN apk add gcompat
COPY --from=build /workspace/build/native/nativeCompile/* app
ENTRYPOINT ./app
