FROM amazoncorretto:21-alpine

LABEL org.opencontainers.image.authors="acme_parking_team-T1-Ziyang-Fang"
LABEL description="Image for the Visitor microservice"
EXPOSE 9080

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=docker", "/app.jar"]
