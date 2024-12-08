
# build stage
FROM gradle:8-jdk22-alpine AS builder

COPY --chown=gradle .. /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build -x test
# Use a base image with Java runtime
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Spring Boot jar file built with Gradle into the container
COPY build/libs/gateway-application-0.0.1-SNAPSHOT.jar application.jar

# Expose the port that your application runs on
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "application.jar"]
