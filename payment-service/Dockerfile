# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file from the target directory to the container
COPY target/payment-service-0.0.1-SNAPSHOT.jar app.jar

# Make the JAR file executable
RUN chmod +x app.jar

# Run the JAR file when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the port your Spring Boot app will run on
EXPOSE 8081
