package com.bilalkristiania.AMQPOrder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.Transient;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImplementation implements OrderService{

    private final OrderRepository orderRepository;

    private final OrderEventPub orderEventPub;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;


    @Value("${amqp.routing.key.inventory}")
    @Transient
    private String inventoryRoutingKey;

    @Override
    public List<Order> getAllOrders() {
        return null;
    }

    @Override
    public Order getOrderById(Long id) {
        return null;
    }

    @Override
    public List<Order> getOrderByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }


    @Transactional
    @Override
    public Order saveOrder(Order order) {

        setOrderAndSubtotal(order);


        return orderRepository.save(order);
    }

    /*
    public Optional<Integer> getProductQuantity(Long productId) {
        final String url = "http://localhost:8084/api/inventory/getProduct/" + productId;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                int quantity = rootNode.path("quantity").asInt();
                log.info(quantity + " this is the quantity from api call");
                return Optional.of(quantity);
            }
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Product not found for productID {}", productId);
        } catch (Exception e) {
            log.error("Error while getting product quantity for productID {}: {}", productId, e.getMessage());
        }
        return Optional.empty();
    }

     */

    public boolean checkUserExists(Long userId) {
        final String url = "http://localhost:8080/api/users/" + userId; // URL of your User Service
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            log.info("Response from User Service for userID {}: {}", userId, response.getBody());
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("User not found for userID {}", userId);
            return false;
        } catch (Exception e) {
            log.error("Error while checking if user exists for userID {}: {}", userId, e.getMessage());
            return false;
        }
    }


    public Order processOrderFromPayment(PaymentEvent paymentEvent){
        // Fetch the order using the order ID from the payment event
        Long orderId = paymentEvent.getOrderId();
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            // Handle the case where the order is not found
            throw new RuntimeException("Order not found with ID: " + orderId);
        }

        Order order = orderOptional.get();

        // Update the order status based on the payment success
        if (paymentEvent.isPaymentSuccessful()) {
            order.setStatus("Completed");
        } else {
            order.setStatus("Payment Failed");
        }


        return orderRepository.save(order);
    }

    /* might cause issues with the code at this moment
    public Order processOrderFromInventory(InventoryEvent inventoryEvent){
        // Fetch the order using the order ID from the payment event
        Long orderId = inventoryEvent.getOrderId();
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            // Handle the case where the order is not found
            throw new RuntimeException("Order not found with ID: " + orderId);
        }

        Order order = orderOptional.get();

        // Update the order status based on the payment success
        if (inventoryEvent.isStockAvailable()) {
            // has to be the same as the paymentEvent since there is stock, and payment takes care of its own logic
            order.setStatus("Completed");
        } else {
            order.setStatus("No Stock");
        }


        return orderRepository.save(order);
    }'

     */


    @Transactional
    @Override
    public Order saveOrderAndSendMsg(Order order){
        setOrderAndSubtotal(order);

        Order result = orderRepository.save(order);
        // check for userId
        if (!checkUserExists(result.getUserId())) {
            // Handle the case where the user does not exist
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        // Create an OrderEvent object from the order
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setId(result.getId());
        orderEvent.setUserId(result.getUserId());
        orderEvent.setProductId(result.getProductId());
        orderEvent.setProductName(result.getProductName());
        orderEvent.setOrderDate(result.getOrderDate());
        orderEvent.setStatus(result.getStatus());
        orderEvent.setProductAmount(result.getProductAmount());
        orderEvent.setTotalAmount(result.getTotalAmount());
        log.info(String.valueOf(orderEvent));
        // Copying equipment items details
        if (result.getOrderItems() != null) {
            List<OrderEvent.EquipmentItemDetails> equipmentItems = result.getOrderItems()
                    .stream()
                    .map(item -> new OrderEvent.EquipmentItemDetails(
                            item.getEquipmentOrderId(),
                            item.getEquipmentId(),
                            item.getEquipmentName(),
                            item.getQuantity(),
                            item.getUnitPrice(),
                            item.getSubtotal()))
                    .collect(Collectors.toList());
            orderEvent.setEquipmentItems(equipmentItems);
        }

        log.info(String.valueOf(orderEvent));
        // Send the OrderEvent to RabbitMQ
        orderEventPub.sendOrderEvent(orderEvent);
        orderEventPub.sendOrderEvent(orderEvent, inventoryRoutingKey);
        return result;
    }

    private void setOrderAndSubtotal(Order order) {
        List<EquipmentItem> items = order.getOrderItems();
        if (items != null) {
            for (EquipmentItem equipmentItem : items) {
                equipmentItem.setOrder(order);
                equipmentItem.setSubtotal(equipmentItem.getUnitPrice() * equipmentItem.getQuantity());
            }
            log.info("OrderItems size: " + items.size());
            double totalAmount = calculateTotalAmount(items, order.getProductAmount());
            order.setTotalAmount(totalAmount);
        } else {
            log.warn("OrderItems is null for order: " + order.getId());
            // Handle the case where order items are null, e.g., set a default total amount
            order.setTotalAmount(order.getProductAmount());
        }
    }

    private double calculateTotalAmount(List<EquipmentItem> items, double productAmount) {
        if (items == null) return productAmount;

        double equipmentTotal = items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();

        return equipmentTotal + productAmount;
    }






}
