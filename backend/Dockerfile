#-------- Stage 1: Build --------
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

#Copy Maven project files
COPY pom.xml .
COPY src ./src

#Package the Spring Boot application
RUN mvn clean package -DskipTests

#-------- Stage 2: Run --------
FROM eclipse-temurin:21-jdk

WORKDIR /app

#Copy the jar from the previous stage
COPY --from=build /app/target/*.jar app.jar

#Expose Spring Boot default port
EXPOSE 8080

#Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]