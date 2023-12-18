# Use JDK 20 as the base image
FROM openjdk:20

# Copy the JAR file into the Docker image
COPY ./target/InventoryService-0.0.1-SNAPSHOT.jar /usr/src/inventoryservice/

# Set the working directory inside the container
WORKDIR /usr/src/inventoryservice

# Expose the port the application runs on
EXPOSE 8084

# Command to run the application
CMD ["java", "-jar", "InventoryService-0.0.1-SNAPSHOT.jar"]