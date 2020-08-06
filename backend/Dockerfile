FROM java:8

LABEL maintainer="ltk3934@gmail.com"

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} videoconference.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/videoconference.jar"]
