package com.skillswap.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentCallbackRequest {

    @NotBlank(message = "Order id is required")
    private String orderId;

    @NotBlank(message = "Gateway transaction id is required")
    private String gatewayTransactionId;

    @NotBlank(message = "Status is required")
    private String status; // "SUCCESS" | "FAILED" | "CANCELLED"
}