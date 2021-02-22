FROM openjdk:15-alpine3.12
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENV PROTOCOL http
ENV PORT 8080
HEALTHCHECK CMD wget -qO- ${PROTOCOL}://localhost:${PORT}/actuator/health -o /dev/null 2>&1 | grep UP || exit 1
ENTRYPOINT ["java", "-jar", "/app.jar"]