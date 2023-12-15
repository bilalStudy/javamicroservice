package com.bilalkristiania.InventoryService;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderEvent {
    // might need refactoring of order class
    private Long id;
    private Long productId;
    private String status;
}
