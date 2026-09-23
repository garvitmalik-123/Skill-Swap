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
@Document(collection = "wallet_transactions")
public class WalletTransaction {

    @Id
    private String id;

    @Indexed
    private String creatorId;

    private WalletTransactionType type;

    private double amount;

    private String orderId;

    private String description;

    @CreatedDate
    private Instant createdAt;

    public enum WalletTransactionType {
        EARNING_PENDING, EARNING_RELEASED, PAYOUT
    }
}