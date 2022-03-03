# syntax=docker/dockerfile:1

FROM openjdk:16-alpine3.13 as base

WORKDIR /registration-app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

FROM base as test
CMD ["./mvn", "test"]

FROM base as devevelopment
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=mysql", "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000"]

FROM base as build
RUN ./mvnw package

FROM openjdk:11-jre-slim as production
EXPOSE 8080
COPY --from=build /registration-app/target/register-*.jar /register.jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/register.jar"]