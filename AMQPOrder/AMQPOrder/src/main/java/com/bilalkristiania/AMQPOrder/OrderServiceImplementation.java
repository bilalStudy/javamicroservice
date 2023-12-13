package com.bilalkristiania.AMQPOrder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

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

        setOrderAndSubtotal(order);


        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order saveOrderAndSendMsg(Order order){
        setOrderAndSubtotal(order);

        Order result = orderRepository.save(order);
        // Create an OrderEvent object from the order
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setId(result.getId());
        orderEvent.setUserId(result.getUserId());
        orderEvent.setProductId(result.getProductId());
        orderEvent.setProductName(result.getProductName());
        orderEvent.setOrderDate(result.getOrderDate());
        orderEvent.setStatus(result.getStatus());
        orderEvent.setProductAmount(result.getProductAmount());
        orderEvent.setTotalAmount(result.getTotalAmount());
        log.info(String.valueOf(orderEvent));
        // Copying equipment items details
        if (result.getOrderItems() != null) {
            List<OrderEvent.EquipmentItemDetails> equipmentItems = result.getOrderItems()
                    .stream()
                    .map(item -> new OrderEvent.EquipmentItemDetails(
                            item.getEquipmentOrderId(),
                            item.getEquipmentId(),
                            item.getEquipmentName(),
                            item.getQuantity(),
                            item.getUnitPrice(),
                            item.getSubtotal()))
                    .collect(Collectors.toList());
            orderEvent.setEquipmentItems(equipmentItems);
        }

        log.info(String.valueOf(orderEvent));
        // Send the OrderEvent to RabbitMQ
        orderEventPub.sendOrderEvent(orderEvent);
        orderEventPub.sendOrderEvent(orderEvent, inventoryRoutingKey);
        return result;
    }

    private void setOrderAndSubtotal(Order order) {
        for (EquipmentItem equipmentItem: order.getOrderItems()) {
            equipmentItem.setOrder(order);
            equipmentItem.setSubtotal(equipmentItem.getUnitPrice()*equipmentItem.getQuantity());
        }


        log.info("OrderItems size: " + order.getOrderItems().size());
        double totalAmount = calculateTotalAmount(order.getOrderItems(), order.getProductAmount());
        order.setTotalAmount(totalAmount);
    }

    private double calculateTotalAmount(List<EquipmentItem> items, double productAmount) {
        double equipmentTotal = items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();

        return equipmentTotal + productAmount;
    }






}
