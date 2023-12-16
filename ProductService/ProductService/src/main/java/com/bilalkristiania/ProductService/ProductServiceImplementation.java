package com.bilalkristiania.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImplementation implements ProductService{

    private final ProductRepository productRepository;
    @Override
    public void processInventoryEvent(InventoryEvent inventoryEvent) {
        try {
            // Attempt to fetch the product by ID
            Optional<Product> optionalProduct = productRepository.findById(inventoryEvent.getProductId());

            if (optionalProduct.isPresent()) {
                Product productFromInventoryEvent = optionalProduct.get();
                log.info("Product found: {}", productFromInventoryEvent);

                // Update the availability of the product
                productFromInventoryEvent.setAvailable(inventoryEvent.isStockAvailable());

                // Save the updated product to the repository
                productRepository.save(productFromInventoryEvent);
            } else {
                log.warn("No product found with ID {}", inventoryEvent.getProductId());
            }
        } catch (Exception e) {
            log.error("Error processing inventory event for product ID {}: {}", inventoryEvent.getProductId(), e.getMessage());
            // Handle the exception appropriately
        }
    }

    @Override
    public Product getProductById(Long id) {
        return null;
    }

    @Override
    public Product saveCar(Product car) {

        return productRepository.save(car);
    }
}
