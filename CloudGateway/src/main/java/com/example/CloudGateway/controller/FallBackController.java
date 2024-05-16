package com.example.CloudGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {

    @GetMapping("/orderServiceFallBack")
    public String orderServiceFallback(){
        return "Order service is dowm!";
    }

    @GetMapping("/paymentServiceFallBack")
    public String paymentServiceFallBack(){
        return "Payment service is dowm!";
    }

    @GetMapping("/productServiceFallBack")
    public String productServiceFallBack(){
        return "Product service is dowm!";
    }

}
