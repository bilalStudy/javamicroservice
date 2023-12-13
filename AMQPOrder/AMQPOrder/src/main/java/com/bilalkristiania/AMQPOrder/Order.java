package com.bilalkristiania.AMQPOrder;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // should include the user that makes this purchase
    private Long userId;
    // need some field somewhere to identify what user it is making this if its userId or userName
    // should maybe also have some field for products
    private Long productId;
    private String productName;
    private String orderDate;
    private String status;
    private double productAmount;
    private double totalAmount;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EquipmentItem> orderItems;
}
