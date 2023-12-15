package com.bilalkristiania.InventoryService;

public interface InventoryService {

    InventoryResult processOrderEvent(OrderEvent orderEvent);
    InventoryResult processInventory(OrderEvent orderEvent);

    //get by id method
    //get all method maybe
    //save method

    Inventory getInventoryById(Long id);

    Inventory getInventoryByProductId(Long productId);

    Inventory saveInventory(Inventory carInventory);

    EquipmentInventory getEquipmentById(Long id);

    EquipmentInventory saveEquipmentById(EquipmentInventory equipmentInventory);

    void sendRabbitInventoryEvent(InventoryEvent inventoryEvent);


}
