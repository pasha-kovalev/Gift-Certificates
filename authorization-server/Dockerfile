FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} authorization-server.jar
ENTRYPOINT ["java","-jar","/authorization-server.jar"]

