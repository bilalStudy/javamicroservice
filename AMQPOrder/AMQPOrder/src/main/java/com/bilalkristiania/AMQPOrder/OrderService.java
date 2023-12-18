package com.bilalkristiania.AMQPOrder;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderById(Long id);

    List<Order> getOrderByUserId(Long userId);

    Order saveOrder(Order order);

    Order saveOrderAndSendMsg(Order order);
}
