FROM alpine:3
RUN apk add gcompat
COPY build/native/nativeCompile/* app
ENTRYPOINT ./app
