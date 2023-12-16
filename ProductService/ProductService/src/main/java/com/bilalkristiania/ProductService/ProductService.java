package com.bilalkristiania.ProductService;

public interface ProductService {

    void processInventoryEvent(InventoryEvent inventoryEvent);

    Product getProductById(Long id);


    Product saveCar(Product car);
}
