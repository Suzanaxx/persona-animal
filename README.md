# 🐾 Persona Animal Backend

A backend service for managing animal personas, built using Java, Spring Boot, PostgreSQL, and Firebase.

## Development Status

This project is actively developed as part of a larger platform for animal persona data management.

---

## Overview

The Persona Animal Backend provides a RESTful API for managing animal-related data, including user authentication via Firebase and persistent storage in PostgreSQL. It supports structured database migrations using Flyway and is designed to be easily deployable in both local and cloud environments.

---

## Features

- ✅ REST API for managing animal persona data  
- 🔐 Authentication and authorization via Firebase  
- 🐘 PostgreSQL support with schema migration using Flyway  
- 🔁 Docker-based development environment  
- 📋 Swagger/OpenAPI support (optional)  
- ⚙️ Environment configuration via `.properties` and environment variables  
- 🚦 Health check endpoint  
- 🧪 Test support with Spring Boot Test  

---

## Getting Started

### Prerequisites

- Java 21  
- Maven 3.8+  
- Docker (for local PostgreSQL)  
- Firebase project with service account JSON  

---

## Running with Docker

Run the PostgreSQL database with Docker:

docker run --name persona-db \
  -e POSTGRES_PASSWORD=your_password \
  -e POSTGRES_DB=persona_animal_database \
  -p 5432:5432 \
  -d postgres:16

## 🛠️Building & Running the App
1. Clone the repository
git clone https://github.com/your-username/persona-animal-backend.git
cd persona-animal-backend
2. Create the application.properties file
properties
PostgreSQL configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/persona_animal_database
spring.datasource.username=postgres
spring.datasource.password=your_password

Firebase
firebase.config.path=src/main/resources/firebase-service-account.json

JPA / Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

Flyway
spring.flyway.enabled=true
⚠️ Do not commit this file to version control. Instead, include an application.properties.example file as a template.

3. Add Firebase service account JSON
Download your Firebase admin SDK key and save it to:

src/main/resources/firebase-service-account.json
4. Run the application
./mvnw spring-boot:run
The server will start on: http://localhost:8080

API Endpoints
Health Check
http
GET /health
Response:
json
{ "status": "ok" }
(Optional) Swagger UI
If enabled, you can access Swagger documentation at:

http://localhost:8080/swagger-ui/index.html
Database Migrations with Flyway

Migration scripts live in:
src/main/resources/db/migration

They are automatically run at startup, or can be triggered manually:
./mvnw flyway:migrate

# Project Structure
Environment Variables
You may override .properties values via environment variables:

Variable	Description	Default
SPRING_DATASOURCE_URL	JDBC URL to PostgreSQL	
SPRING_DATASOURCE_USERNAME	DB username	
SPRING_DATASOURCE_PASSWORD	DB password	
FIREBASE_CONFIG_PATH	Path to Firebase JSON config	

# Testing
Run tests with:
./mvnw test

# 🐾 Persona Animal Frontend

Frontend aplikacija je zgrajena z uporabo **React + TypeScript** in omogoča uporabnikom interaktivno samoocenjevanje, ocenjevanje drugih oseb ter prikaz kompatibilnosti osebnosti v obliki simboličnih živali.

### ⚙️ Tehnologije
- ⚛️ React (TypeScript)
- 🔥 Firebase Authentication
- 🎨 CSS/Tailwind za oblikovanje
- 🍃 REST API integracija (do backend-a)
- ⚡ Vite kot dev strežnik

---

### 🧩 Ključne funkcionalnosti

| Funkcija                   | Opis                                                                 |
|----------------------------|----------------------------------------------------------------------|
| 🔐 **Prijava**             | Firebase Google login integracija                                     |
| 🧠 **Samoocenitev**        | Uporabnik izbere živali, ki predstavljajo njega samega               |
| 👤 **Ocenjevanje drugih**  | Ocenjevanje osebnosti drugih oseb preko vizualnih izborov           |
| 🤝 **Primerjava**          | Kompatibilnost med dvema ocenjenima osebnostma                      |
| 📜 **Zgodovina**           | Prikaz preteklih samoocenitev in ocen drugih oseb                   |

---
