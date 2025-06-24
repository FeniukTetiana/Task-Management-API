# Task Management API
This is a Spring Boot application that provides a RESTful API for managing tasks. It includes features like task creation, retrieval, updates, deletion, and robust error handling. The API also incorporates internationalization (i18n) and basic authentication for security.

## Features
- CRUD Operations: Full Create, Read, Update, and Delete functionality for tasks.
- Task Status Management: Tasks have different statuses with defined valid transitions.
- Data Validation: Input validation for task creation and updates.
- RESTful API: Clearly defined endpoints for task management.
- Internationalization (i18n): Supports multiple languages for API messages and descriptions (currently configured for Ukrainian and English based on the messages.properties file).
- Basic Authentication: Secure API endpoints with username/password authentication.
- Role-Based Access Control: Different roles (ADMIN, USER) have varying permissions for API actions.
- H2 Database: In-memory database for easy setup and testing.
- Swagger/OpenAPI: API documentation automatically generated for easy exploration and testing.
- Global Exception Handling: Centralized error handling for a consistent API response.

## Technologies Used
- Spring Boot: Framework for building the application.
- Spring Data JPA: For database interaction.
- H2 Database: In-memory database.
- Spring Security: For authentication and authorization.
- MapStruct: For mapping between DTOs and entities.
- Lombok: To reduce boilerplate code.
- Swagger UI/OpenAPI: For API documentation.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
- Java 17 or newer
- Maven
- 
### Installation
1) Clone the repository:
```bash:
git clone <repository_url>
```
2) Build the project:
```bash:
mvn clean package
```
### Running the Application
You can run the application using Maven:
```bash:
mvn spring-boot:run
```
The application will start on port 8080 by default.

## API Documentation (Swagger UI)
Once the application is running, you can access the API documentation at:
http://localhost:8080/swagger-ui.html
Here you can explore all available endpoints, their descriptions, request/response models, and even test them directly from the browser.

## Authentication
This API uses HTTP Basic Authentication.

Default Credentials:
- Admin User:
Username: admin
Password: admin
- Regular User:
Username: user
Password: user

You will need to provide these credentials in your API requests (e.g., using an Authorization header with Basic <base64\_encoded\_username:password>).

###  API Endpoints
All task-related endpoints are under the /tasks path.

| HTTP Method | Endpoint        | Description                     | Required Role |
|-------------|------------------|----------------------------------|----------------|
| GET         | `/tasks`         | Get all tasks                   | ADMIN, USER    |
| POST        | `/tasks`         | Create a new task               | ADMIN          |
| GET         | `/tasks/{id}`    | Get a task by ID                | ADMIN, USER    |
| PUT         | `/tasks/{id}`    | Update an existing task         | ADMIN          |
| PATCH       | `/tasks/{id}`    | Partially update a task         | USER           |
| DELETE      | `/tasks/{id}`    | Delete a task by ID             | ADMIN          |

## Internationalization (i18n)
The API supports internationalization. You can specify the desired language by adding a lang query parameter to your requests.

Example:

To get responses in Ukrainian: http://localhost:8080/tasks?lang=uk
To get responses in English (default): http://localhost:8080/tasks?lang=en
The messages are defined in the src/main/resources/messages.properties and src/main/resources/messages_uk.properties files.

## Database
The application uses an in-memory H2 database. You can access the H2 console at:

http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password: (leave blank)

A default task will be inserted into the database upon application startup if no tasks exist.

## Error Handling
The API provides consistent error responses:
- 400 Bad Request: For invalid input (e.g., validation errors).
- 401 Unauthorized: If no authentication credentials are provided or they are invalid.
- 403 Forbidden: If the authenticated user does not have the necessary permissions.
- 404 Not Found: When a requested resource (e.g., task by ID) does not exist.
- 500 Internal Server Error: For unexpected server errors.