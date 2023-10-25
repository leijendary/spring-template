FROM alpine:3
RUN apk add gcompat
COPY build/native/nativeCompile/* app
RUN chmod +x ./app
ENTRYPOINT ./app
