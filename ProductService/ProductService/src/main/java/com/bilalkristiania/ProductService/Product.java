package com.bilalkristiania.ProductService;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // car brand
    // car name
    // car productionYear
    // car color maybe enum later
    // car fuel
    // car transmission
    // mileage would be relevant if we sold used cars, but we dont
    // maybe feature list

    private String carName;
    private String manufacturer;
    private String color;
    private String fuel;
    private String transmission;
    private BigDecimal price;
    private String description;
    private boolean available;
}
