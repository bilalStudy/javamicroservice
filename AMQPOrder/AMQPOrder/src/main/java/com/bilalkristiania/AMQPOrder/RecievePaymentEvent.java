package com.bilalkristiania.AMQPOrder;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RecievePaymentEvent {

    OrderServiceImplementation orderServiceImplementation;

    private final OrderEventPub orderEventPub;

    @Autowired
    public RecievePaymentEvent(OrderServiceImplementation orderServiceImplementation, OrderEventPub orderEventPub) {
        this.orderServiceImplementation = orderServiceImplementation;
        this.orderEventPub = orderEventPub;
    }

    @RabbitListener(queues = "${amqp.queue.payment}") // Replace with the actual queue name
    public void receivePaymentEvent(PaymentEvent paymentEvent) {
        log.info("Received Payment Event in Order Service: {}", paymentEvent);
    }

    //need to move this to another class or rename the class to listener
    @RabbitListener(queues = "${amqp.queue.inventory}")
    public void receiveInventoryEvent(InventoryEvent inventoryEvent){
        log.info("Recieved Inventory Event in Order Service: {}", inventoryEvent);
    }
}
