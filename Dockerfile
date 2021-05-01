FROM adoptopenjdk/openjdk11-openj9:alpine

VOLUME /tmp

ARG MAIN_CLASS=com.leijendary.spring.microservicetemplate.Application
ARG DEPENDENCY=target/dependency

ENV MAIN_CLASS=${MAIN_CLASS}

COPY ${DEPENDENCY}/BOOT-INF/classes /app
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF

ENTRYPOINT java -Xshareclasses -Xquickstart -cp app:app/lib/* ${MAIN_CLASS}
