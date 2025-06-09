#Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jdk
ARG JAR_FILE=target/*.jar
#Copy the built jar file to the container
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar
#Run the jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]