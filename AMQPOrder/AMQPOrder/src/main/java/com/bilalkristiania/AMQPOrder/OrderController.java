package com.bilalkristiania.AMQPOrder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    private final EquipmentItemRepository equipmentItemRepository;

    OrderController(OrderRepository orderRepository, OrderService orderService, EquipmentItemRepository equipmentItemRepository){
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.equipmentItemRepository = equipmentItemRepository;
    }

    @GetMapping("/orders")
    public List<Order> allOrders(){
        return (List<Order>) orderRepository.findAll();
    }

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable Long id){
        return orderRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Didnt find order with ID" + id));
    }

    @GetMapping("/orders/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrderByUserId(userId);
        if (orders.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orders);
    }


    @PostMapping("/orders")
    public Order newOrder(@RequestBody Order order){

        //return orderRepository.save(order);
        return orderService.saveOrder(order);
    }

    @PostMapping("/orders/save")
    public Order newOrderAndSendEvent(@RequestBody Order order) {
        return orderService.saveOrderAndSendMsg(order);
    }



}

