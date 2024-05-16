package com.company.ProductService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data

@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;
}
