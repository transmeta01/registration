# syntax=docker/dockerfile:1

FROM openjdk:16-alpine3.13

WORKDIR /registration-app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.arguments=--logging.level.org.springframework=TRACE"]