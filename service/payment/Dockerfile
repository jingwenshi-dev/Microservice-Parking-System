FROM openjdk:23-jdk

RUN mkdir /data
VOLUME /data
EXPOSE 9090

ARG JAR_FILE=target/*-exec.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=docker", "/app.jar"]