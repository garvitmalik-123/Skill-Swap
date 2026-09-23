package com.skillswap.backend.dto.response;

import com.skillswap.backend.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private String id;
    private String orderNumber;
    private String buyerId;
    private String courseId;
    private double amount;
    private Order.OrderStatus status;
    private Instant createdAt;
}