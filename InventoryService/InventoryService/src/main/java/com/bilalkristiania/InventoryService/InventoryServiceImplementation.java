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
        Optional<Inventory> inventoryOpt = getInventoryByProductId(productIdFromOrder);

        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            boolean isStockAvailable = inventory.getQuantity() > 0;

            if (isStockAvailable){
                int newQuantity = inventory.getQuantity() - 1;
                inventory.setQuantity(newQuantity);
                inventoryRepository.save(inventory);
            }else {
                log.warn("Inventory for Product ID {} is out of stock", productIdFromOrder);
            }
            //use inventory related to the productId and then take minus 1 from the stock in db
            return new InventoryResult(inventory.getProductId(),inventory.getQuantity(), isStockAvailable);
        } else {
            log.warn("Inventory not found for Product ID: {}", productIdFromOrder);
            return new InventoryResult(productIdFromOrder, 0, false);
        }
    }


    // gets inventory ID but should also have a method to getInventoryByProductID
    @Override
    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Didnt find product with ID" + id));
    }


    public Optional<Inventory> getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
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
        Optional<Inventory> inventoryOpt = getInventoryByProductId(inventoryEvent.getProductId());

        if (inventoryOpt.isPresent()) {
            long inventoryId = inventoryOpt.get().getId();
            inventoryEvent.setInventoryId(inventoryId);
        } else {
            log.warn("Inventory item not found for Product ID: {}", inventoryEvent.getProductId());
            inventoryEvent.setInventoryId(-1L); // Set to -1 or any other appropriate value to indicate not found
            // If you want to stop the method execution here, use 'return;'
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
