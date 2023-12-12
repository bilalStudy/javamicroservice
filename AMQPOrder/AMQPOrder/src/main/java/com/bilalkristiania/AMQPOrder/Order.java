package com.bilalkristiania.AMQPOrder;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    private String date;
    private String status;
    private String name;
    private String amount;
}
