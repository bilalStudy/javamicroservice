package com.bilalkristiania.AMQPOrder;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class RecievePaymentEvent {

    OrderServiceImplementation orderServiceImplementation;

    @Autowired
    public RecievePaymentEvent(OrderServiceImplementation orderServiceImplementation) {
        this.orderServiceImplementation = orderServiceImplementation;
    }

    @RabbitListener(queues = "${amqp.queue.payment}") // Replace with the actual queue name
    public void receiveOrderEvent(PaymentEvent paymentEvent) {
        log.info("Received Order Event in Payment Service: {}", paymentEvent);
    }
}
