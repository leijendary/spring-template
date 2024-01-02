FROM azul/zulu-openjdk-alpine:21-jre-headless-latest
COPY build/libs/*.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
