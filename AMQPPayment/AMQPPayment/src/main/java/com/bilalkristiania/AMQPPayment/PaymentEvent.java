package com.bilalkristiania.AMQPPayment;

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
    private String name;
    private String amount;
}
