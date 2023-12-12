package com.bilalkristiania.OrderService;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Entity

public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;
    private Long productId;
    private String productName;
    // quantity is not as relevant for the car itself as it is for equipment like tires or such.
    private int quantity;
    private double unitPrice;
    private double subtotal;
    @ManyToOne
    @JoinColumn(name = "id")  // This should match the name of the field in the OrderItems table
    //@JsonIgnore // using JsonIgnore to not cause recursion in save method.
    private Order order;
}
