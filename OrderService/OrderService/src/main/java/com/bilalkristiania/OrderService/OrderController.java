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
    private final EquipmentItemRepository equipmentItemRepository;

    OrderController(OrderRepository orderRepository, EquipmentItemRepository equipmentItemRepository){
        this.orderRepository = orderRepository;
        this.equipmentItemRepository = equipmentItemRepository;
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


    private double calculateTotalAmount(List<EquipmentItem> items, double productAmount) {
        double equipmentTotal = items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();

        return equipmentTotal + productAmount;
    }

    @PostMapping("/orders")
    public Order newOrder(@RequestBody Order order){

        for (EquipmentItem equipmentItem: order.getOrderItems()) {
            equipmentItem.setOrder(order);
            equipmentItem.setSubtotal(equipmentItem.getUnitPrice()*equipmentItem.getQuantity());
        }


        log.info("OrderItems size: " + order.getOrderItems().size());
        double totalAmount = calculateTotalAmount(order.getOrderItems(), order.getProductAmount());
        order.setTotalAmount(totalAmount);


        return orderRepository.save(order);
    }

}
