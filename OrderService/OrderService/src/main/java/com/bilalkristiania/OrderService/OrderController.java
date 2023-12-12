package com.bilalkristiania.OrderService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    OrderController(OrderRepository orderRepository, OrderItemRepository orderItemRepository){
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @GetMapping("/orders")
    public List<Order> allProducts(){
        return orderRepository.findAll();
    }

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable Long id){
        return orderRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Didnt find product with ID" + id));
    }


    private double calculateTotalAmount(List<OrderItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();
    }

    @PostMapping("/orders")
    public Order newOrder(@RequestBody Order order){


        //was needed because i didnt have an OrderItemRepository
        /*
        for (OrderItems orderItems: order.getOrderItems()) {
            orderItems.setOrder(order);
        }
        order.setOrderItems(order.getOrderItems());

         */

        log.info("OrderItems size: " + order.getOrderItems().size());
        double totalAmount = calculateTotalAmount(order.getOrderItems());
        order.setTotalAmount(totalAmount);


        return orderRepository.save(order);
    }

}
