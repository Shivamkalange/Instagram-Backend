FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]


# Use the following command to build the Docker image:
# docker build -t instagram-backend .
# Use the following command to run the Docker container:
# docker run -p 8080:8080 instagram-backend