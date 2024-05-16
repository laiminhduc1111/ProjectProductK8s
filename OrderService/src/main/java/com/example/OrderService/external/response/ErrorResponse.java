package com.example.OrderService.external.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;
}
