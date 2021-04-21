FROM openjdk:8-jre-alpine
WORKDIR /tmp
COPY ./target/user-application.jar /tmp/user-application.jar
EXPOSE 8080
CMD [ "java", "-jar", "user-application.jar" ]