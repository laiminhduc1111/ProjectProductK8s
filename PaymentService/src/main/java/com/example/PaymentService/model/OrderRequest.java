package com.example.PaymentService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private long productId;

    private long totalAmount;

    private long quantity;

    private long orderId;

    private PaymentMode paymentMode;
}
