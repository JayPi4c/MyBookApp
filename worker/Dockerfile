# Phase 1: Build
FROM eclipse-temurin:25-jdk-alpine AS build

# Create and copy app files
RUN mkdir /app
COPY . /app

# Set work directory
WORKDIR /app

# Ensure Maven wrapper is executable
RUN chmod +x ./mvnw

# Build the application
RUN ./mvnw clean package -pl worker -am -DskipTests --activate-profiles docker

# Phase 2: Runtime
FROM eclipse-temurin:25-jre-alpine

# Set work directory
WORKDIR /opt/demo

# Create a group and user for better security
RUN addgroup -S demo && \
    adduser -S -G demo demo && \
    chown -R demo:demo /opt/demo

# Create /app folder for files
RUN  mkdir /app && \
    chown -R demo:demo /app

# Copy the JAR from the build stage
COPY --from=build /app/worker/target/worker.jar worker.jar

# Change to 'demo' user
USER demo

# Run the application
ENV SPRING_PROFILES_ACTIVE=docker
CMD java -jar worker.jar