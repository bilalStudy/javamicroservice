package com.bilalkristiania.AMQPPayment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderEvent {
    // the id field is for the orderId
    private Long id;
    private Long productId;
    private String status;
    private double totalAmount;
}

