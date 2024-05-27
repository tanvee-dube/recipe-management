FROM amazoncorretto:17-al2023-jdk
COPY target/recipemanager-0.0.1-SNAPSHOT.jar recipemanager.jar
ENTRYPOINT ["java","-jar","/recipemanager.jar"]
EXPOSE 8085