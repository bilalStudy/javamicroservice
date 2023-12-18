# Use JDK 20 as the base image
FROM openjdk:20

# Copy the JAR file into the Docker image
COPY ./target/ProductService-0.0.1-SNAPSHOT.jar /usr/src/productservice/

# Set the working directory inside the container
WORKDIR /usr/src/productservice

# Expose the port the application runs on
EXPOSE 8081

# Command to run the application
CMD ["java", "-jar", "ProductService-0.0.1-SNAPSHOT.jar"]
