package com.bilalkristiania.AMQPOrder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImplementation implements OrderService{

    private final OrderRepository orderRepository;

    private final OrderEventPub orderEventPub;

    @Value("${amqp.routing.key.inventory}")
    private String inventoryRoutingKey;

    @Override
    public List<Order> getAllOrders() {
        return null;
    }

    @Override
    public Order getOrderById(Long id) {
        return null;
    }

    @Transactional
    @Override
    public Order saveOrder(Order order) {
        Order result = orderRepository.save(order);

        // TODO: here I can call the publish class to send a message to rabbit

        return result;
    }

    @Transactional
    @Override
    public Order saveOrderAndSendMsg(Order order){
        Order result = orderRepository.save(order);

        // Create an OrderEvent object from the order
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setId(result.getId());
        orderEvent.setStatus(result.getStatus());
        orderEvent.setName(result.getName());
        orderEvent.setDate(result.getDate());
        orderEvent.setAmount(result.getAmount());

        // Send the OrderEvent to RabbitMQ
        orderEventPub.sendOrderEvent(orderEvent);
        orderEventPub.sendOrderEvent(orderEvent, inventoryRoutingKey);
        return result;
    }




}
