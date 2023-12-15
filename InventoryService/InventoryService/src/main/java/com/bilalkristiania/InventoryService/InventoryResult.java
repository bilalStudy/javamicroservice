package com.bilalkristiania.InventoryService;


import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResult {
    private Long productId;
    private int quantity;
    private boolean isStockAvailable;
}
