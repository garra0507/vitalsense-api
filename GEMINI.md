# VitalSense API - Project Instructions

## Project Overview
VitalSense API is a Spring Boot-based backend for a healthcare platform. It manages medical services including doctor schedules, patient records, appointments, and an integrated AI assistant.

### Tech Stack
- **Language:** Java 21
- **Framework:** Spring Boot 4.0.6
- **Database:** PostgreSQL 16
- **Security:** Spring Security + JWT (JSON Web Tokens)
- **API Documentation:** Springdoc OpenAPI (Swagger)
- **Containerization:** Docker & Docker Compose
- **Build Tool:** Maven

### Architecture
The project follows a **Package-by-Feature** architecture. Each business domain is encapsulated in its own package under `com.biotech.vitalsenseapi`, containing its own internal layers:
- `auth`: Authentication, JWT logic, and Security configuration.
- `appointment`: Appointment scheduling and management.
- `doctor` / `patient`: Profile management for users.
- `assistant`: AI-driven assistant services.
- `shared`: Global configurations, exception handling, and common utilities.

## Building and Running

### Prerequisites
- Java 21 JDK
- Docker Desktop (for database)

### Key Commands
- **Start Infrastructure:** `docker-compose up -d`
- **Build Project:** `./mvnw clean compile`
- **Run Application:** `./mvnw spring-boot:run`
- **Run Tests:** `./mvnw test`

### Database Access
- **Local Database:** `localhost:55432` (PostgreSQL)
- **pgAdmin:** `http://localhost:8082` (User: `admin@vitalsense.com`, Pass: `admin`)

## Development Conventions

### API Standards
- **RESTful Endpoints:** All APIs follow REST conventions.
- **DTOs:** Use Data Transfer Objects for request/response bodies.
- **Security:** Most endpoints require a Bearer Token. Use the "Authorize" button in Swagger UI to test protected endpoints.

### Coding Style
- **Lombok:** Used extensively for boilerplate reduction (`@Data`, `@Builder`, `@RequiredArgsConstructor`).
- **Global Exception Handling:** Managed in `com.biotech.vitalsenseapi.shared.exception`.
- **Validation:** Use `jakarta.validation` annotations on DTOs.

### Git Workflow
- Always develop in feature branches (e.g., `feat/`, `fix/`).
- Do not commit directly to `main` or `develop`.

## Future Roadmap
- **AI Integration:** Implementing an AI assistant using Spring AI for natural language appointment scheduling.
- **Frontend:** Angular-based web application (separate repository `vitalsense-web`).
