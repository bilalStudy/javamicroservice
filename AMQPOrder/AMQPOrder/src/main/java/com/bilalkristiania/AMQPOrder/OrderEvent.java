package com.bilalkristiania.AMQPOrder;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class OrderEvent {
    // Fields from Order
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private String orderDate;
    private String status;
    private double productAmount;
    private double totalAmount;

    // List of EquipmentItem objects
    private List<EquipmentItemDetails> equipmentItems;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class EquipmentItemDetails {
        // Fields from EquipmentItem
        private Long equipmentOrderId;
        private Long equipmentId;
        private String equipmentName;
        private int quantity;
        private double unitPrice;
        private double subtotal;
    }
}
