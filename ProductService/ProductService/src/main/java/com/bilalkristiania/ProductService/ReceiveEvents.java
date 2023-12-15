package com.bilalkristiania.ProductService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReceiveEvents {

    ProductServiceImplementation productServiceImplementation;


    @Autowired
    public ReceiveEvents(ProductServiceImplementation productServiceImplementation) {
        this.productServiceImplementation = productServiceImplementation;
    }

    // might need to create another binding for this class and also implement method for processing availability for product based on inventory
    @RabbitListener(queues = "${amqp.queue.inventory}") // Replace with the actual queue name
    public void receiveOrderEvent(InventoryEvent inventoryEvent) {
        log.info("Received Order Event in Inventory Service: {}", inventoryEvent);
    }
}
