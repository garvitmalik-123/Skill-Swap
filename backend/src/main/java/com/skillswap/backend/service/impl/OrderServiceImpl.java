package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.request.CreateOrderRequest;
import com.skillswap.backend.dto.request.PaymentCallbackRequest;
import com.skillswap.backend.dto.response.OrderResponse;
import com.skillswap.backend.entity.*;
import com.skillswap.backend.entity.Notification.NotificationType;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.exception.ResourceNotFoundException;
import com.skillswap.backend.repository.*;
import com.skillswap.backend.service.NotificationService;
import com.skillswap.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CourseRepository courseRepository;
    private final CreatorWalletRepository creatorWalletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final NotificationService notificationService;

    @Override
    public OrderResponse createOrder(String buyerId, CreateOrderRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (course.getType() != Course.CourseType.PAID) {
            throw new BadRequestException("This course does not require a paid order");
        }

        Order order = Order.builder()
                .orderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                .buyerId(buyerId)
                .courseId(course.getId())
                .amount(course.getPrice())
                .status(Order.OrderStatus.PENDING)
                .build();

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    @Override
    public OrderResponse handlePaymentCallback(PaymentCallbackRequest request) {
        // Idempotency: if this gateway transaction was already processed, return the existing result.
        if (paymentRepository.existsByGatewayTransactionId(request.getGatewayTransactionId())) {
            Payment existing = paymentRepository.findByGatewayTransactionId(request.getGatewayTransactionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
            Order order = orderRepository.findById(existing.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
            return toResponse(order);
        }

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new BadRequestException("Order has already been processed");
        }

        Payment.PaymentStatus paymentStatus;
        Order.OrderStatus orderStatus;

        switch (request.getStatus().toUpperCase()) {
            case "SUCCESS" -> {
                paymentStatus = Payment.PaymentStatus.SUCCESS;
                orderStatus = Order.OrderStatus.PAID;
            }
            case "CANCELLED" -> {
                paymentStatus = Payment.PaymentStatus.CANCELLED;
                orderStatus = Order.OrderStatus.CANCELLED;
            }
            default -> {
                paymentStatus = Payment.PaymentStatus.FAILED;
                orderStatus = Order.OrderStatus.FAILED;
            }
        }

        Payment payment = Payment.builder()
                .orderId(order.getId())
                .gatewayTransactionId(request.getGatewayTransactionId())
                .amount(order.getAmount())
                .status(paymentStatus)
                .build();
        paymentRepository.save(payment);

        order.setStatus(orderStatus);
        Order savedOrder = orderRepository.save(order);

        if (orderStatus == Order.OrderStatus.PAID) {
            creditCreatorWallet(order);

            courseRepository.findById(order.getCourseId()).ifPresent(course -> {
                notificationService.notify(
                        order.getBuyerId(),
                        NotificationType.PAYMENT_RECEIVED,
                        "Payment successful",
                        "Your payment for \"" + course.getTitle() + "\" was successful",
                        order.getId(),
                        "ORDER");

                notificationService.notify(
                        course.getCreatorId(),
                        NotificationType.PAYMENT_RECEIVED,
                        "You made a sale!",
                        "Someone purchased your course \"" + course.getTitle() + "\"",
                        order.getId(),
                        "ORDER");
            });
        }

        return toResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return toResponse(order);
    }

    private void creditCreatorWallet(Order order) {
        Course course = courseRepository.findById(order.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        CreatorWallet wallet = creatorWalletRepository.findByCreatorId(course.getCreatorId())
                .orElseGet(() -> creatorWalletRepository.save(
                        CreatorWallet.builder().creatorId(course.getCreatorId()).build()));

        double commission = order.getAmount() * wallet.getCommissionRate();
        double creatorShare = order.getAmount() - commission;

        wallet.setPendingEarnings(wallet.getPendingEarnings() + creatorShare);
        creatorWalletRepository.save(wallet);

        WalletTransaction transaction = WalletTransaction.builder()
                .creatorId(course.getCreatorId())
                .type(WalletTransaction.WalletTransactionType.EARNING_PENDING)
                .amount(creatorShare)
                .orderId(order.getId())
                .description("Earning from order " + order.getOrderNumber())
                .build();
        walletTransactionRepository.save(transaction);
    }

    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .buyerId(order.getBuyerId())
                .courseId(order.getCourseId())
                .amount(order.getAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}