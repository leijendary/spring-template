FROM azul/zulu-openjdk-alpine:17-latest as build
WORKDIR /workspace/app
COPY src src
COPY gradle gradle
COPY build.gradle.kts .
COPY gradlew .
COPY settings.gradle.kts .
RUN --mount=type=cache,target=/root/.m2 ./gradlew build --console plain -x test
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)

FROM azul/zulu-openjdk-alpine:17-jre-latest
VOLUME /app
ARG DEPENDENCY=/workspace/app/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENV JAVA_TOOL_OPTIONS "-Dcom.sun.management.jmxremote.ssl=false \
    -Dcom.sun.management.jmxremote.authenticate=false \
    -Dcom.sun.management.jmxremote.port=9875 \
    -Dcom.sun.management.jmxremote.rmi.port=9875 \
    -Dcom.sun.management.jmxremote.host=0.0.0.0 \
    -Djava.rmi.server.hostname=0.0.0.0"
ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.leijendary.spring.template.ApplicationKt"]
