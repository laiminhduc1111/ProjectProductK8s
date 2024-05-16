package com.example.OrderService.service;

import com.example.OrderService.entity.Order;
import com.example.OrderService.exception.CustomException;
import com.example.OrderService.external.client.PaymentService;
import com.example.OrderService.external.client.ProductService;
import com.example.OrderService.external.request.PaymentRequest;
import com.example.OrderService.external.response.PaymentResponse;
import com.example.OrderService.external.response.ProductReponse;
import com.example.OrderService.model.OrderRequest;
import com.example.OrderService.model.OrderResponse;
import com.example.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;


    @Override
    public long placeOrder(OrderRequest orderRequest) {
        log.info("Place Order:{}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());
        Order order = new Order();
        try {
            order = Order.builder()
                    .amount(orderRequest.getTotalAmount())
                    .orderStatus("CREATED")
                    .productId(orderRequest.getProductId())
                    .quantity(orderRequest.getQuantity())
                    .orderDate(Instant.now())
                    .build();

            order = orderRepository.save(order);
        } catch (Exception e) {
            order.setOrderStatus("FAILED");
            orderRepository.save(order);
        }

        log.info("Call Payment service to complete the payment");
        PaymentRequest paymentRequest =
                PaymentRequest.builder()
                        .orderId(order.getId())
                        .paymentMode(orderRequest.getPaymentMode())
                        .amount(orderRequest.getTotalAmount())
                        .build();
        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done");
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.error("Payment failed");
            orderStatus = "PAYMENT_FAILED";
        }
        order.setOrderStatus(orderStatus);

        orderRepository.save(order);
        log.info("Order successfully with order Id", order.getId());
        return order.getId();
    }

    OrderResponse.ProductDetails productDetails;
    OrderResponse.PaymentDetails paymentDetails;

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get detail for orderID :{}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found " + orderId, "NOT_FOUND", 404));
        try {
            ProductReponse productReponse = productService.getProductById(order.getProductId());
            productDetails =
                    OrderResponse.ProductDetails.builder()
                            .productName(productReponse.getProductName())
                            .productId(productReponse.getProductId())
                            .quantity(productReponse.getQuantity())
                            .price(productReponse.getPrice())
                            .build();
        } catch (Exception e) {
            log.info(e);
        }

        try {
            log.info("Getting payment information from payment service");
            PaymentResponse paymentResponse = paymentService.getPaymentDetails(String.valueOf(order.getId()));

            paymentDetails = OrderResponse.PaymentDetails.builder()
                    .paymentId(paymentResponse.getPaymentId())
                    .paymentDate(paymentResponse.getPaymentDate())
                    .paymentMode(paymentResponse.getPaymentMode())
                    .status(paymentResponse.getStatus())
                    .amount(paymentResponse.getAmount())
                    .orderId(paymentResponse.getOrderId())
                    .build();
        } catch (Exception e) {
            log.info(e);
        }


        OrderResponse orderResponse =
                OrderResponse.builder()
                        .orderId(order.getId())
                        .orderStatus(order.getOrderStatus())
                        .amount(order.getAmount())
                        .orderDate(order.getOrderDate())
                        .productDetails(productDetails)
                        .paymentDetails(paymentDetails)
                        .build();

        return orderResponse;
    }

}
