FROM alpine:3
RUN apk add gcompat
COPY --chmod=755 build/native/nativeCompile/* app
ENTRYPOINT ./app
