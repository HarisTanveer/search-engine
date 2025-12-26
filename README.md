# Obvio Backend API

This project provides a backend API for document ingestion and TF-IDF based search functionality.

## Setup Instructions

Follow these steps to set up and run the project locally using Docker:

1.  **Build the application JAR:**
    Open your terminal in the project root directory and run the following command:
    ```bash
    ./gradlew build
    ```
    This will compile the Java code and package it into a JAR file located in `build/libs/`.

2.  **Start the application and PostgreSQL database using Docker Compose:**
    After the build is complete, use Docker Compose to spin up the application and its PostgreSQL dependency:
    ```bash
    docker compose up
    ```
    This command will:
    *   Build the Docker image for the application (if not already built or if changes were made).
    *   Start a PostgreSQL database container.
    *   Start the Spring Boot application container, connected to the database.

    The application will be accessible on `http://localhost:8080`.

## API Endpoints

*   **Ingestion:** (Details to be added, e.g., POST /ingest endpoint)
*   **Search:** (Details to be added, e.g., GET /search?word={word} endpoint)