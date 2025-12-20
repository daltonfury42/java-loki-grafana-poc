# Use a lightweight Java runtime
FROM eclipse-temurin:17-jdk

# Set the working directory
WORKDIR /app

# Copy your jar file into the image
# REPLACE 'your-app.jar' with your actual filename
COPY target/java-app-1.0-SNAPSHOT.jar app.jar

# IMPORTANT: Ensure your Java app logs to STDOUT/STDERR
# Promtail (the log scraper) reads logs from the container's standard output.
CMD ["java", "-jar", "app.jar"]