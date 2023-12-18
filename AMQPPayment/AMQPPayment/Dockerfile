# Use JDK 20 as the base image
FROM openjdk:20

# Copy the JAR file into the Docker image
COPY ./target/AMQPPayment-0.0.1-SNAPSHOT.jar /usr/src/amqppayment/

# Set the working directory inside the container
WORKDIR /usr/src/amqppayment

# Expose the port the application runs on
EXPOSE 8083

# Command to run the application
CMD ["java", "-jar", "AMQPPayment-0.0.1-SNAPSHOT.jar"]
