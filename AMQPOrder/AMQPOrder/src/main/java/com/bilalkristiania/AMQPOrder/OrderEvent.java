package com.bilalkristiania.AMQPOrder;

import lombok.*;

@Getter
@Setter
public class OrderEvent {
    private Long id;
    private String status;
    private String name;
    private String date;
    private String amount;


}
