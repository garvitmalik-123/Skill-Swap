package com.skillswap.backend.repository;

import com.skillswap.backend.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PaymentRepository extends MongoRepository<Payment, String> {

    Optional<Payment> findByGatewayTransactionId(String gatewayTransactionId);

    boolean existsByGatewayTransactionId(String gatewayTransactionId);

    Optional<Payment> findByOrderId(String orderId);
}