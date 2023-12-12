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
    private String status;
    private String name;
    private String date;
    private String amount;
}
