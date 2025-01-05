FROM gradle:8.12-jdk21 AS build

workdir /app

COPY . .

RUN gradle build --no-daemon

FROM openjdk:21-jdk-slim

WORKDIR /app
ENV APP_VERSION="1.5.2"
LABEL maintainer="vl_softeng"
COPY --from=build /app/build/libs/task-0.0.1-SNAPSHOT.jar /app/task-0.0.1-SNAPSHOT.jar
USER nobody
ENTRYPOINT ["java", "-jar", "/app/task-0.0.1-SNAPSHOT.jar"]
