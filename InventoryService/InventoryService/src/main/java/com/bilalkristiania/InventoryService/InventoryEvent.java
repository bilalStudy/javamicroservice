package com.bilalkristiania.InventoryService;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class InventoryEvent {
    private long inventoryId;
    private long orderId;
    private long productId;
    private int quantity;
    private boolean isStockAvailable;
    private String status;

}
