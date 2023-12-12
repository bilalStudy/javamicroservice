package com.bilalkristiania.AMQPOrder;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPub {

    private final AmqpTemplate amqpTemplate;
    private final String orderExchange; // Rename to match your exchange name

    // Define your routing key as needed
    // You can adjust this based on your needs
    @Value("${amqp.routing.key.order}")
    private String routingKey;

    public OrderEventPub(final AmqpTemplate amqpTemplate,
                         @Value("${amqp.exchange.order}") final String orderExchange) {
        this.amqpTemplate = amqpTemplate;
        this.orderExchange = orderExchange;
    }



    public void sendOrderEvent(final OrderEvent orderEvent, String routingKey) {
        OrderEvent event = buildEvent(orderEvent);
        amqpTemplate.convertAndSend(orderExchange, routingKey, event);
    }

    // defaults to sending the event with the payment routing key, might change this to inventory later

    public void sendOrderEvent(final OrderEvent orderEvent) {
        OrderEvent event = buildEvent(orderEvent);
        amqpTemplate.convertAndSend(orderExchange, routingKey, event);
    }

    private OrderEvent buildEvent(final OrderEvent inputEvent) {
        OrderEvent event = new OrderEvent();
        // Perform any necessary event transformation here
        event.setId(inputEvent.getId());
        event.setStatus(inputEvent.getStatus());
        event.setName(inputEvent.getName());
        event.setDate(inputEvent.getDate());
        event.setAmount(inputEvent.getAmount());
        return event;
    }
}
