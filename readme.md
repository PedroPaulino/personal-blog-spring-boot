# About 

This is a Blog WebApp created using Spring Boot, PostgreSQL, Docker and other technologies.

For more details about the Architecture, Technologies, Testing, CI/CD and how to execute this project, check the sections below.

## Architecture

### Technology Stack

1. Frontend:
    - HTML
    - CSS
    - JavaScript

2. Backend:
    - Java 17
    - Spring Boot
    - Spring Web MVC
    - Spring Security
    - Jakarta Validation

3. Database & Persistence:
    - PostgreSQL
    - Spring Data JPA
    - Hibernate
    - H2 (In-memory DB)

4. Tests:
    - JUnit
    - Mockito
    - Spring Security Test
    - H2 (In-memory DB)

5. Build:
    - Maven

6. Containers:
    - Docker
    - GitHub Container Registry - GHCR (Used to image registry)

7. CI/CD:
    - GitHub Actions

### Development & Publishing Process

![alt](/docs/Personal%20Blog%20Flow.jpg)

## How to execute this project

### Requirements

1. Git
2. Docker
3. Java 17+
4. Apache Maven 3.9+

### A. Locally with Docker

1. Clone this repository

```git clone [repository-url]```
```cd personal-blog```

2. Generate JAR file: 

```./mvnw clean package```

3. Set-up the .env file using .env-example file with the environment variables to be used on Docker

4. Use Docker Compose to build and start all services: 

```docker compose up --build```

5. Access the frontend service on: 

```http://localhost:3000/```

6. You can look at the Backend API on: 

```http://localhsot:8080/```

*Stop* You can stop the application by running:

```docker compose down```

### B. Using GitHub Container Registry

1. Clone this repository

```git clone [repository-url]```
```cd personal-blog```

2. Set-up the .env file using .env-example file with the environment variables to be used on Docker

3. Use Docker Compose to pull the latest image: 

```docker compose -f docker-compose.ghcr-yml pull```

4. Use Docker Compose to start all services:

```docker compose -f docker-compose.ghcr.yml up```

Alternatively, you can use the ```-d --force-recreate``` flags along with the command above to make sure the container will be recreated and attached using latest image.

5. Access the frontend service on: 

```http://localhost:3000/```

*Stop* You can stop the application by running:

```docker compose -f docker-compose.ghcr.yml down```