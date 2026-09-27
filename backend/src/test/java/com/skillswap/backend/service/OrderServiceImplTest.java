package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.request.PaymentCallbackRequest;
import com.skillswap.backend.dto.response.OrderResponse;
import com.skillswap.backend.entity.Order;
import com.skillswap.backend.entity.Payment;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CreatorWalletRepository creatorWalletRepository;

    @Mock
    private WalletTransactionRepository walletTransactionRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void handlePaymentCallback_shouldReturnExistingOrder_whenGatewayTransactionAlreadyProcessed() {
        Payment existingPayment = Payment.builder()
                .id("payment-1")
                .orderId("order-1")
                .gatewayTransactionId("gw-txn-123")
                .status(Payment.PaymentStatus.SUCCESS)
                .build();

        Order existingOrder = Order.builder()
                .id("order-1")
                .orderNumber("ORD-ABC123")
                .status(Order.OrderStatus.PAID)
                .build();

        PaymentCallbackRequest request = new PaymentCallbackRequest();
        request.setOrderId("order-1");
        request.setGatewayTransactionId("gw-txn-123");
        request.setStatus("SUCCESS");

        when(paymentRepository.existsByGatewayTransactionId("gw-txn-123")).thenReturn(true);
        when(paymentRepository.findByGatewayTransactionId("gw-txn-123")).thenReturn(Optional.of(existingPayment));
        when(orderRepository.findById("order-1")).thenReturn(Optional.of(existingOrder));

        OrderResponse response = orderService.handlePaymentCallback(request);

        assertThat(response.getStatus()).isEqualTo(Order.OrderStatus.PAID);
        // Duplicate callback should NOT trigger a second wallet credit or order update.
    }

    @Test
    void handlePaymentCallback_shouldThrowBadRequest_whenOrderAlreadyProcessed() {
        Order alreadyPaidOrder = Order.builder()
                .id("order-1")
                .status(Order.OrderStatus.PAID)
                .build();

        PaymentCallbackRequest request = new PaymentCallbackRequest();
        request.setOrderId("order-1");
        request.setGatewayTransactionId("gw-txn-new");
        request.setStatus("SUCCESS");

        when(paymentRepository.existsByGatewayTransactionId("gw-txn-new")).thenReturn(false);
        when(orderRepository.findById("order-1")).thenReturn(Optional.of(alreadyPaidOrder));

        assertThatThrownBy(() -> orderService.handlePaymentCallback(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already been processed");
    }
}