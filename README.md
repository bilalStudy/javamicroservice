
# Microservices Application Setup Guide

## Overview
This guide outlines the setup process for a microservices-based application involving various services like User, Product, Order, Payment, and Inventory, along with a Gateway service. The application utilizes RabbitMQ, Consul, and H2 database, and is managed through Maven and Docker.

## Prerequisites
- RabbitMQ
- Consul
- H2 Database
- Maven
- Docker

## Service Ports
- User Service (springtest): `8080`
- Product Service (Car product): `8081`
- Order Service (AMQPOrder): `8082`
- Payment Service (AMQPayment): `8083`
- Inventory Service: `8084`
- Gateway Service: `9080`
- Frontend: `1234`

## Local Setup
1. **RabbitMQ & Consul**: Start RabbitMQ and Consul. Consul can be started with `consul agent -node=learnmicro -dev`. On Windows, use `.\consul agent -node=learnmicro -dev`.
2. **Maven**: Navigate to the microservice directory (e.g., `AMQPOrder/AMQPOrder`) and run `mvn clean install` to generate the `target` folder with a `.jar` file.
3. **Microservice Startup**: Start all microservices. Note that services like AMQPOrder, AMQPPayment, Inventory, and Product Service might crash initially due to non-existent queues. Restart them after the initial run.

## Data Initialization
Use Postman or curl commands to initialize data:
- **User**: POST `http://localhost:8080/api/users`
  ```json
  {
    "name": "Jon Jones",
    "address": "Hollywood Street",
    "age": 36
  }
  ```
- **Product Service**: POST `http://localhost:8081/api/products`
  ```json
  {
    "carName": "Car Model X",
    "manufacturer": "JavaGuar",
    "color": "black",
    "fuel": "electric",
    "transmission": "automatic",
    "price": 5000000.00,
    "description": "JavaGuars newest edition, SUV 4 wheel drive",
    "available": true
  }
  ```
- **Inventory**: POST `http://localhost:8084/api/inventory`
  ```json
  {
    "productId": 1,
    "quantity": 200
  }
  ```
- **Order**: POST `http://localhost:8082/api/orders/save`
```json

{
  "userId": 1,
  "productId": 1,
  "productName": "Car Model X",
  "orderDate": "2023-12-15",
  "status": "Pending",
  "productAmount": 20000.0,
  "orderItems": [
    {
      "equipmentId": 101,
      "equipmentName": "Tires",
      "quantity": 4,
      "unitPrice": 100.0,
      "subtotal": 0.0
    },
    {
      "equipmentId": 102,
      "equipmentName": "Sunroof",
      "quantity": 1,
      "unitPrice": 500.0,
      "subtotal": 0.0
    }
  ]
}
```


## Payment Info
And we could also create a POST to payment at http://localhost:8083/api/payment but this takes only one account for the transaction and that is the account with id 1, if you don't create it before the service exists but have done all the other posts it will automatically create the account with a balance of 4000000

## Additional info
I would recommend creating all these various posts to save before trying to send an order.

## Dockerization
To dockerize the application:you cd into the respective microservice and do docker build -t microservice name that corresponds with the docker-compose.yml and then add a punctuation so it builds from the dockerfile in the directory you are in. as an example in my docker-compose the AMQPOrder service is named amqporder:latest so then I need that image

1. Navigate to the microservice directory.
2. Run `docker build -t [microservice name]:latest .`.
3. In `docker/h2` and `docker/consul`, run `docker build -t h2-server:2.1.214 .` and `docker build -t consul-importer:1.9 .`, i also did `docker build -t consul .` but i dont know if that is needed respectively.

## Running Docker Compose
- Use `docker compose -f docker-compose.yml up --build` in the `docker/` folder.
- Alternatively, to run without microservices use `docker compose -f docker-compose-no-microservices.yml up --build`.

## Additional Notes
I hope there is some lenience on the docker part since I used a total of 6 hours to make it run on my machine. Despite it saying that it wasn't possible to run wsl(windows subsystem for linux) on windows 10 home. And my pc not having support for hyper-v virtualization. Despite this it should still be running.

## Application Workflow
For how the microservice application works. When everything of the data is set at the company for luxury cars, it will initiate both synchronous calls and asynchronous calls when an order from the client is sent. This order will then check if the userId exists with a synchronous REST api call and also send the OrderEvent if the user exists to the InventoryService and PaymentService with asynchronous event driven communication using RabbitMQ. In the inventory we will see if there is stock for the productId given by the order, and then return true if we do have stock aswell as remove one from the quantity for that product. This inventory service also sends an event to the product service and updates the availability of the product to false if we are out of stock. The orderevent is also sent from the order to the payment, the payment does a rest call to the inventory service with the productId given and checks for stock with a REST call, if we do indeed have stock we will process the payment and send it back to order. In order after payment, the order is saved as completed. The call from payment to inventory to check for stock should in theory not happen but I will talk more about this in the reflection documentation.
