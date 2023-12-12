package com.bilalkristiania.InventoryService;

public interface InventoryService {

    boolean processOrderEvent(OrderEvent orderEvent);
    boolean processPayment(OrderEvent orderEvent);

    //get by id method
    //get all method maybe
    //save method

    Inventory getInventoryById(Long id);

    Inventory saveInventory(Inventory carInventory);

    EquipmentInventory getEquipmentById(Long id);

    EquipmentInventory saveEquipmentById(EquipmentInventory equipmentInventory);


}
