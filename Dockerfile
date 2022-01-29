FROM gradle:7-jdk11 as build
WORKDIR /workspace/app
COPY src src
COPY build.gradle.kts .
COPY settings.gradle.kts .
RUN --mount=type=cache,target=/root/.m2 gradle bootJar -x test
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)

FROM openjdk:11-jre-slim
VOLUME /app
ARG DEPENDENCY=/workspace/app/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.leijendary.spring.boot.template.ApplicationKt"]
