# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim AS runtime

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the target directory to /app
COPY target/*.jar app.jar

# Expose the port your Spring Boot application runs on
EXPOSE 8080

# Optimize JVM settings and allow profile-based execution
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "app.jar"]
