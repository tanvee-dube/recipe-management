# Recipe Bank


Recipe Bank exposes methods to create, retrieve and searchByCriteria for recipes.
This application leverages layered architecture.

# Build Docker Image
``./mvnw spring-boot:build-image``

# Run Docker Image
`` docker run -p 8080:8080 recipebank``
# To Do
1 Segregate in-memory H2 database by creating changelogs for database management.
2 Secure the apis.