package com.bilalkristiania.AMQPPayment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderEvent {
    private Long id;
    private String status;
    private String name;
    private String date;
    private String amount;
}

