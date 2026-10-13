# MemberShield

MemberShield is an explainable payment-risk assessment application designed to explore how banks can identify potentially dangerous transfers while giving customers clear, understandable reasons for additional checks.

The application will combine a Kotlin and Spring Boot backend with a React frontend. Its first release will use deterministic risk rules rather than machine learning, allowing every decision to be tested, audited and explained.

> MemberShield is a portfolio project built with synthetic data. It must not be used to make real financial decisions.

## Current status

The initial backend foundation is complete and includes:

- A Kotlin and Spring Boot application running on Java 21
- A versioned REST endpoint
- Spring Boot Actuator health monitoring
- A focused web-layer test
- A full application-context test

## Technology

### Backend

- Kotlin
- Spring Boot
- Gradle
- JUnit 5
- Spring MockMvc
- Spring Boot Actuator

### Planned

- PostgreSQL
- Flyway database migrations
- Docker and Docker Compose
- React
- GitHub Actions
- OpenAPI documentation

## Repository structure

```text
membershield/
├── backend/       # Kotlin and Spring Boot API
├── frontend/      # React application (planned)
└── README.md
```

## Running the backend

Requirements:

- Java 21

From the repository root:

```bash
cd backend
./gradlew bootRun
```

The API will start at:

```text
http://localhost:8080
```

Check the application status:

```bash
curl http://localhost:8080/api/v1/status
```

Expected response:

```json
{
  "service": "membershield-api",
  "status": "UP"
}
```

Check the operational health endpoint:

```bash
curl http://localhost:8080/actuator/health
```

## Running the tests

From the `backend` directory:

```bash
./gradlew test
```

To run the complete verification lifecycle from a clean build:

```bash
./gradlew clean check
```

## Roadmap

- Define the payment-assessment domain
- Implement configurable risk rules
- Return plain-English reasons for triggered risk signals
- Persist assessments and audit history in PostgreSQL
- Add duplicate-payment protection
- Build customer confirmation and cancellation workflows
- Create an accessible React customer journey
- Add automated CI checks and containerised local development