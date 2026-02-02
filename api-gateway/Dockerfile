FROM eclipse-temurin:21-jre-ubi10-minimal

WORKDIR /app

# Copy from Gradle build directory
COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]