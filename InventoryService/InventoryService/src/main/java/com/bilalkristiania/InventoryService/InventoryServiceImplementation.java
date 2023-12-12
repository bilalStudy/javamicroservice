package com.bilalkristiania.InventoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@Service
public class InventoryServiceImplementation implements InventoryService{

    private final InventoryRepository inventoryRepository;
    private final EquipmentInventoryRepository equipmentInventoryRepository;

    @Override
    public boolean processOrderEvent(OrderEvent orderEvent) {
        return false;
    }

    @Override
    public boolean processPayment(OrderEvent orderEvent) {
        return false;
    }

    @Override
    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Didnt find product with ID" + id));
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
}
