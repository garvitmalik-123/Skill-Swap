package com.skillswap.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    @Indexed
    private String orderId;

    /** External payment gateway's transaction/callback id — used to dedupe callbacks. */
    @Indexed(unique = true, sparse = true)
    private String gatewayTransactionId;

    private double amount;

    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    private String failureReason;

    @CreatedDate
    private Instant createdAt;

    public enum PaymentStatus {
        PENDING, SUCCESS, FAILED, CANCELLED
    }
}