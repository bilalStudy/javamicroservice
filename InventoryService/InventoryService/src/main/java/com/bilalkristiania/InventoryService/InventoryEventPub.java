package com.bilalkristiania.InventoryService;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InventoryEventPub {
    private final AmqpTemplate amqpTemplate;
    private final String inventoryExchange; // Rename to match your exchange name


    // Define your routing key as needed
    // You can adjust this based on your needs
    @Value("${amqp.routing.key.inventory}")
    private String routingKey;


    public InventoryEventPub(final AmqpTemplate amqpTemplate, @Value("${amqp.exchange.inventory}") String inventoryExchange) {
        this.amqpTemplate = amqpTemplate;
        this.inventoryExchange = inventoryExchange;
    }


    public void sendInventoryEvent(final InventoryEvent inventoryEvent) {
        InventoryEvent event = buildEvent(inventoryEvent);
        amqpTemplate.convertAndSend(inventoryExchange, routingKey, event);
    }

    public void sendInventoryEvent(final InventoryEvent inventoryEvent, String routingKey) {
        InventoryEvent event = buildEvent(inventoryEvent);
        amqpTemplate.convertAndSend(inventoryExchange, routingKey, event);
    }


    private InventoryEvent buildEvent(final InventoryEvent inputEvent) {
        InventoryEvent event = new InventoryEvent();
        // Perform any necessary event transformation here
        event.setInventoryId(inputEvent.getInventoryId());
        event.setOrderId(inputEvent.getOrderId());
        event.setProductId(inputEvent.getProductId());
        event.setQuantity(inputEvent.getQuantity());
        event.setStockAvailable(inputEvent.isStockAvailable());
        event.setStatus(inputEvent.getStatus());
        return event;
    }

}
