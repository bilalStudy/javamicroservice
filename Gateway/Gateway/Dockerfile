# Use JDK 20 as the base image
FROM openjdk:20

# Copy the JAR file into the Docker image
COPY ./target/Gateway-0.0.1-SNAPSHOT.jar /usr/src/gateway/

# Set the working directory inside the container
WORKDIR /usr/src/gateway

# Expose the port the application runs on
EXPOSE 9080

# Command to run the application
CMD ["java", "-jar", "Gateway-0.0.1-SNAPSHOT.jar"]
