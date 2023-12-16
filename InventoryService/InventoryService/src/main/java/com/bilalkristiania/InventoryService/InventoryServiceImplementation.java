package com.bilalkristiania.InventoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class InventoryServiceImplementation implements InventoryService{

    private final InventoryRepository inventoryRepository;
    private final InventoryEventPub inventoryEventPub;

    @Value("${amqp.routing.key.product}")
    private String productRoutingKey;


    @Override
    public InventoryResult processOrderEvent(OrderEvent orderEvent) {
        log.info("Processing Order Event: {}", orderEvent);
        InventoryResult inventoryResult = processInventory(orderEvent);
        boolean isStockAvailable = inventoryResult.isStockAvailable();
        long productId = inventoryResult.getProductId();
        int quantity = inventoryResult.getQuantity();
        log.info("Processed Order Event: {}", orderEvent);
        return new InventoryResult(productId, quantity, isStockAvailable);
    }

    @Override
    public InventoryResult processInventory(OrderEvent orderEvent) {
        Long productIdFromOrder = orderEvent.getProductId();
        Inventory inventoryResult = getInventoryByProductId(productIdFromOrder);
        boolean isStockAvailable = inventoryResult.getQuantity() > 0;
        int quantity = inventoryResult.getQuantity();
        Long productIdFromDatabase = inventoryResult.getProductId();
        return new InventoryResult(productIdFromDatabase, quantity, isStockAvailable );
    }


    // gets inventory ID but should also have a method to getInventoryByProductID
    @Override
    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Didnt find product with ID" + id));
    }


    public Inventory getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Didn't find inventory with Product ID: " + productId));
    }




    @Override
    public Inventory saveInventory(Inventory carInventory) {
        return null;
    }

    @Override
    public EquipmentInventory getEquipmentById(Long id) {
        return null;
    }

    @Override
    public EquipmentInventory saveEquipmentById(EquipmentInventory equipmentInventory) {
        return null;
    }


    //might need to make this method void, instead of returning InventoryEvent
    @Override
    public void sendRabbitInventoryEvent(InventoryEvent inventoryEvent) {
        try {
            long inventoryId = getInventoryByProductId(inventoryEvent.getProductId()).getId();
            inventoryEvent.setInventoryId(inventoryId);
        } catch (ResponseStatusException e) {
            // Handle the exception, e.g., log it or set a default value
            log.error("Inventory item not found for Product ID: {}", inventoryEvent.getProductId(), e);

            // Optionally, you might want to set a default value or take other actions
            inventoryEvent.setInventoryId(-1L); // Example: set to -1 to indicate not found
            // You can also return from the method or throw a custom exception if needed
            return;
        }

        // Continue with setting other fields and sending events
        inventoryEvent.setOrderId(inventoryEvent.getOrderId());
        inventoryEvent.setProductId(inventoryEvent.getProductId());
        inventoryEvent.setQuantity(inventoryEvent.getQuantity());
        inventoryEvent.setStockAvailable(inventoryEvent.isStockAvailable());
        inventoryEvent.setStatus(inventoryEvent.getStatus());

        // Sending the inventoryEvent to the stock routing key
        inventoryEventPub.sendInventoryEvent(inventoryEvent);
        // Sending the inventoryEvent to the other routing key
        inventoryEventPub.sendInventoryEvent(inventoryEvent, productRoutingKey);
    }


}
