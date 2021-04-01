FROM adoptopenjdk/openjdk11-openj9:alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Xshareclasses", "-Xquickstart", "-jar", "/app.jar"]