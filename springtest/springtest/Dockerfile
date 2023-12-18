# Use JDK 20 as the base image
FROM openjdk:20

# Copy the JAR file into the Docker image
COPY ./target/springtest-0.0.1-SNAPSHOT.jar /usr/src/springtest/

# Set the working directory inside the container
WORKDIR /usr/src/springtest

# Expose the port the application runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "Springtest-0.0.1-SNAPSHOT.jar"]
