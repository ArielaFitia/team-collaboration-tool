# Team Collaboration Tool

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup Instructions](#setup-instructions)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Future Enhancements](#future-enhancements)
- [Contributing](#contributing)

---

## Introduction
The **Team Collaboration Tool** is a backend application designed to help teams organize and manage their projects, members, and tasks. It provides CRUD functionality for Teams, Projects, Members, and Tasks while allowing nested interactions between these entities.

---

## Features
- **Teams**: Create and manage teams.
- **Projects**: Assign projects to specific teams.
- **Members**: Add members to teams and assign them tasks.
- **Tasks**: Create, update, and track tasks assigned to members.
- **API Documentation**: Fully documented APIs with Swagger.
- **Error Handling**: Robust error handling with meaningful responses.
- **Data Validation**: Ensures data integrity with validations.
- **Scalability**: Ready for enhancements such as role-based access control and advanced filtering.

---

## Technologies Used
- **Spring Boot 3.4.1**
- **Java 21**
- **PostgreSQL** for the database
- **ModelMapper** for DTO mapping
- **JPA/Hibernate** for ORM
- **Swagger** for API documentation
- **JUnit** for testing
- **Maven** for dependency management

---

## Setup Instructions
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/ArielaFitia/team-collaboration-tool.git
   cd team-collaboration-tool
   ```

2. **Setup Your Environment**:
    - Ensure you have Java 21, Maven, and PostgreSQL installed.
    - Create a PostgreSQL database for the application.
      ```sql
      CREATE DATABASE team_collaboration_tool;
      ```

3. **Update the `application.properties` File**:
   Update the database configuration in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/team_collaboration_tool
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   ```

4. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Access Swagger Documentation**:
    - Once the application is running, navigate to `http://localhost:8080/swagger-ui.html` to view and interact with the API documentation.

---

## API Documentation
The application uses Swagger to provide interactive API documentation. Visit `http://localhost:8080/swagger-ui.html` for details about the endpoints, request/response structures, and examples.

---

## Testing
The application includes unit tests for the service layer. Run the tests with:
```bash
mvn test
```

---

## Future Enhancements
- Implement role-based access control (e.g., Admin, Manager, Developer).
- Add advanced filtering and search capabilities for tasks.
- Introduce pagination and sorting on list endpoints.
- Deploy the application to a cloud platform.

---

## Contributing
Contributions are welcome! Feel free to open issues or submit pull requests to improve the project.

---