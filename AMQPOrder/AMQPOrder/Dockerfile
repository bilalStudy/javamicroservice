# Use JDK 20 as the base image
FROM openjdk:20

# Copy the JAR file into the Docker image
COPY ./target/AMQPOrder-0.0.1-SNAPSHOT.jar /usr/src/amqporder/

# Set the working directory inside the container
WORKDIR /usr/src/amqporder

# Expose the port the application runs on
EXPOSE 8082

# Command to run the application
CMD ["java", "-jar", "AMQPOrder-0.0.1-SNAPSHOT.jar"]
