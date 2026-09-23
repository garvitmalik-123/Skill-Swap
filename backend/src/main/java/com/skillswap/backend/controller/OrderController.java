package com.skillswap.backend.controller;

import com.skillswap.backend.dto.request.CreateOrderRequest;
import com.skillswap.backend.dto.request.PaymentCallbackRequest;
import com.skillswap.backend.dto.response.ApiResponse;
import com.skillswap.backend.dto.response.OrderResponse;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createOrder(currentUser.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable String orderNumber) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getOrderByNumber(orderNumber)));
    }

    /** Sandbox/test payment gateway callback endpoint. */
    @PostMapping("/payment-callback")
    public ResponseEntity<ApiResponse<OrderResponse>> paymentCallback(
            @Valid @RequestBody PaymentCallbackRequest request) {
        OrderResponse response = orderService.handlePaymentCallback(request);
        return ResponseEntity.ok(ApiResponse.success("Payment processed", response));
    }
}