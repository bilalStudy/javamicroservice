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

    @RabbitListener(queues = "${amqp.queue.order.to.inventory}") // Replace with the actual queue name
    public void receiveOrderEvent(OrderEvent orderEvent) {
        log.info("Received Order Event in Inventory Service: {}", orderEvent);
        InventoryResult inventoryResult = inventoryServiceImplementation.processOrderEvent(orderEvent);
        long productIdFromOrderEvent = inventoryResult.getProductId();
        int quantity = inventoryResult.getQuantity();
        boolean isStockAvailable = inventoryResult.isStockAvailable();



        InventoryEvent inventoryEvent = new InventoryEvent();
        // the method for set inventoryId need to be automatic in the sendRabbitEvent from the service
        inventoryEvent.setOrderId(orderEvent.getId());
        inventoryEvent.setProductId(productIdFromOrderEvent);
        inventoryEvent.setStatus(orderEvent.getStatus());
        inventoryEvent.setQuantity(quantity);
        inventoryEvent.setStockAvailable(isStockAvailable);
        //might change this, if we want to send custom message from inventoryService
        inventoryEvent.setStatus(orderEvent.getStatus());

        log.info("Test Log After Received Order Process in RecieveOrder{}", orderEvent);
        log.info("Also loggining InventoryEvent{}", inventoryEvent);
        inventoryServiceImplementation.sendRabbitInventoryEvent(inventoryEvent);

    }

}
