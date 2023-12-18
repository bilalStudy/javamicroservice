package com.bilalkristiania.InventoryService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class InventoryController {
    private final InventoryRepository inventoryRepository;

    private final InventoryService inventoryService;

    InventoryController(InventoryRepository inventoryRepository, InventoryService inventoryService){
        this.inventoryRepository = inventoryRepository;
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventory")
    public List<Inventory> allInventory(){
        return inventoryRepository.findAll();
    }

    @GetMapping("/inventory/{id}")
    public Inventory getInventoryById(@PathVariable Long id){
        return inventoryService.getInventoryById(id);
    }

    @GetMapping("/inventory/getProduct/{productId}")
    public ResponseEntity<Inventory> getProductWithIdFromInventory(@PathVariable Long productId) {
        Optional<Inventory> inventoryOpt = inventoryService.getInventoryByProductId(productId);

        // Respond with a 404 Not Found status if the inventory item is not found
        return inventoryOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/inventory")
    public Inventory newProduct(@RequestBody Inventory inventory){
        return inventoryRepository.save(inventory);
    }

}
