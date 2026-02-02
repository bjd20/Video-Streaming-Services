# Use official OpenJDK image
FROM eclipse-temurin:21-jre-ubi10-minimal

# Set working directory
WORKDIR /app

# Copy the jar file from Gradle build directory
COPY build/libs/*.jar app.jar

# Expose port
EXPOSE 8761

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]