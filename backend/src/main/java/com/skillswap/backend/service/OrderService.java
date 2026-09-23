package com.skillswap.backend.service;

import com.skillswap.backend.dto.request.CreateOrderRequest;
import com.skillswap.backend.dto.request.PaymentCallbackRequest;
import com.skillswap.backend.dto.response.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(String buyerId, CreateOrderRequest request);

    OrderResponse handlePaymentCallback(PaymentCallbackRequest request);

    OrderResponse getOrderByNumber(String orderNumber);
}