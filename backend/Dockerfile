#Use an official OpenJDK runtime as a parent image
FROM openjdk:23-jdk-alpine
ARG JAR_FILE=target/*.jar
#Copy the built jar file to the container
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar
#Run the jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]