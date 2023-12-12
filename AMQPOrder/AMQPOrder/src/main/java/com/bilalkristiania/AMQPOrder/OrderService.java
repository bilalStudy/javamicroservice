package com.bilalkristiania.AMQPOrder;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderById(Long id);

    Order saveOrder(Order order);

    Order saveOrderAndSendMsg(Order order);
}
