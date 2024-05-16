package com.company.ProductService.controller;

import com.company.ProductService.entity.Product;
import com.company.ProductService.model.ProductReponse;
import com.company.ProductService.model.ProductRequest;
import com.company.ProductService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping
    public ResponseEntity<Long> addProduct(@RequestBody ProductRequest productRequest) {
        long productId = productService.addProduct(productRequest);
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }
    

    @GetMapping("/{id}")
    public ProductReponse getProductById(@PathVariable("id") long productId) {
        ProductReponse productReponse
                = productService.getProductById(productId);
        return productReponse;
    }

    @GetMapping
    public ResponseEntity<List<ProductReponse>> getAllProducts() {
        List<ProductReponse> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PutMapping("/reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(
            @PathVariable("id") long productId,
            @RequestParam long quantity
    ) {
        productService.reduceQuantity(productId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
