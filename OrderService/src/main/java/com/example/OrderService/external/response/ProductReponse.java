package com.example.OrderService.external.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class ProductReponse {
    private String productName;
    private long productId;
    private long quantity;
    private long price;
}
