package com.bilalkristiania.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImplementation implements ProductService{
    @Override
    public Product getProductById(Long id) {
        return null;
    }

    @Override
    public Product saveCar(Product car) {
        return null;
    }
}
