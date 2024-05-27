
# Recipe Management System

A system built to manage, store, and retrieve recipes with ease. Designed using the Layered Architectural Design pattern, maintainability, and efficient data access.

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Setup guide](#setup-guide)
- [Minimum Requirements](#minimum-requirements)
- [Technologies](#technologies)
- [Implementation](#implementation)
- [To Do](#to-do)

## Architecture

**Layered Architectural Design Pattern**:  
The system is organized into distinct layers to separate concerns:

1. **View/Controller Layer**: Manages the GUI and user interactions.
2. **Service Layer**: Handles the business logic of the application, making use of DTOs (Data Transfer Objects) for efficient data operations.
3. **DAO Layer**: Interfaces with the database for CRUD operations.

This separation ensures that changes in one layer don't necessarily impact others, making the system more maintainable and scalable.

## Features

- **Search Recipes**: Quick search using the number of servings,type,ingredient and instructions to retrieve all relevant recipes.
- **CRUD Operations**: Create, Read,Delete and Update operations for recipes.
- **Error Handling**: Common handling of exceptions with error messages.

## Setup guide

#### Minimum Requirements

- Java 17
- Maven 3.x

#### Install the application

1. Make sure you have [Java](https://www.oracle.com/java/technologies/downloads/#jdk17).
2. Open the command line in the source code folder.

3. Build project

  ```
  $ mvn clean install
  ```

Run the tests
  ```
  $ mvn test
  ```


Run the project

  ```
  $ java -jar app.jar
  ```

4. Open the swagger-ui with the link below

```text
http://localhost:8085/openapi/swagger-ui/index.html
```

## Technologies

Java 17, Spring Boot 3.2, Spring Data JPA, H2 Database, OpenAPI, Swagger-UI, Maven, Docker,JUnit and Mockito


-----------------------------------------
## Implementation
- OpenAPI to demonstrate the contract-first approach.
- Dockerization to make it production ready optimally.
- Spring Boot web application using RESTFul Services to handle client requests.
- Spring Data JPA interfaces to persist data in H2 database.
- Swagger-UI to make the APIs readable.
- Unit and integration tests.


## To Do
- Security
- Logging - log statements and trails.
- Improve code coverage with more test cases.


