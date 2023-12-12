package com.bilalkristiania.InventoryService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @PostMapping("/inventory")
    public Inventory newProduct(@RequestBody Inventory inventory){
        return inventoryRepository.save(inventory);
    }

}
