package com.bilalkristiania.InventoryService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReceiveOrderEvent {

    InventoryServiceImplementation inventoryServiceImplementation;

    @Autowired
    public ReceiveOrderEvent(InventoryServiceImplementation inventoryServiceImplementation) {
        this.inventoryServiceImplementation = inventoryServiceImplementation;
    }

    @RabbitListener(queues = "${amqp.queue.order}") // Replace with the actual queue name
    public void receiveOrderEvent(OrderEvent orderEvent) {
        log.info("Received Order Event in Inventory Service: {}", orderEvent);
    }
}
