package com.bilalkristiania.AMQPOrder;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

        // Copying basic order details
        event.setId(inputEvent.getId());
        event.setUserId(inputEvent.getUserId());
        event.setProductId(inputEvent.getProductId());
        event.setProductName(inputEvent.getProductName());
        event.setOrderDate(inputEvent.getOrderDate());
        event.setStatus(inputEvent.getStatus());
        event.setProductAmount(inputEvent.getProductAmount());
        event.setTotalAmount(inputEvent.getTotalAmount());

        // Copying equipment items details
        if (inputEvent.getEquipmentItems() != null) {
            List<OrderEvent.EquipmentItemDetails> equipmentItems = inputEvent.getEquipmentItems()
                    .stream()
                    .map(item -> new OrderEvent.EquipmentItemDetails(
                            item.getEquipmentOrderId(),
                            item.getEquipmentId(),
                            item.getEquipmentName(),
                            item.getQuantity(),
                            item.getUnitPrice(),
                            item.getSubtotal()))
                    .collect(Collectors.toList());
            event.setEquipmentItems(equipmentItems);
        }

        // Perform any additional necessary event transformation here

        return event;
    }

}
