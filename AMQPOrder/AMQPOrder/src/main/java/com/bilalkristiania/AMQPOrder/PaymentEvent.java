package com.bilalkristiania.AMQPOrder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentEvent {
    private long paymentId;
    private long orderId;
    private boolean paymentSuccessful;
    private String status;
    private String totalAmount;
}