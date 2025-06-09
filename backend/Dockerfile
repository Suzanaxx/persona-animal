#Use an official OpenJDK runtime as a parent image
FROM openjdk:23.0.2-jdk-alpine

#Copy the built jar file to the container
COPY target/backend-0.0.1-SNAPSHOT.jar backend-0.0.1-SNAPSHOT.jar

#Run the jar file
ENTRYPOINT ["java", "-jar", "/backend-0.0.1-SNAPSHOT.jar"]