package com.bilalkristiania.AMQPOrder;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    OrderController(OrderRepository orderRepository, OrderService orderService){
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public List<Order> allOrders(){
        return (List<Order>) orderRepository.findAll();
    }

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable Long id){
        return orderRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Didnt find product with ID" + id));
    }

    @PostMapping("/orders")
    public Order newOrder(@RequestBody Order order){

        //return orderRepository.save(order);
        return orderService.saveOrder(order);
    }

    @PostMapping("/orders/save")
    public void newOrderAndSendEvent(@RequestBody Order order) {
        orderService.saveOrderAndSendMsg(order);
    }


}

