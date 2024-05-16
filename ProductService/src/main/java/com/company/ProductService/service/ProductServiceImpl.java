package com.company.ProductService.service;

import com.company.ProductService.entity.Product;
import com.company.ProductService.model.ProductReponse;
import com.company.ProductService.model.ProductRequest;
import com.company.ProductService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.company.ProductService.exeption.ProductServiceCustomException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding Product...");

        Product product
                = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);

        return product.getProductId();
    }

    @Override
    public ProductReponse getProductById(long productId) {
        log.info("Get the product for productId: {}", productId);

        Product product
                = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException("Product with given id not found", "PRODUCT_NOT_FOUND"));
        ProductReponse productReponse
                = new ProductReponse();
        copyProperties(product, productReponse);
        return productReponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce Quantity {} for id {}", quantity, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException(
                        "Product with given id not found",
                        "PRODUCT_NOT_FOUND"));
        if (product.getQuantity() < quantity){
            throw  new ProductServiceCustomException(
                    "Product does not have sufficient Quantity",
                    "INSUFFICIENT_QUANTITY"
            );
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Product quantity updated successfully");

    }

    @Override
    public List<ProductReponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductReponse> productReponses = new ArrayList<>();
        for(Product product: products){
            ProductReponse productReponse = new ProductReponse();
            copyProperties(product,productReponse);
            productReponses.add(productReponse);
        }

        return productReponses;
    }

}

