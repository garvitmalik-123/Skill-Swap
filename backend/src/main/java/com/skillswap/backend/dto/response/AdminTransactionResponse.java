package com.skillswap.backend.dto.response;

import lombok.*;

import java.time.Instant;

@Data
@Builder
public class AdminTransactionResponse {
    private String id;
    private String source;      // "SKILLPOINT" or "WALLET"
    private String userId;      // userId or creatorId depending on source
    private String type;        // CREDIT/DEBIT or the wallet transaction type
    private double amount;
    private String description;
    private Instant createdAt;
}