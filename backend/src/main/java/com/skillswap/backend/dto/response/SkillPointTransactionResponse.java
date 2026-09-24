package com.skillswap.backend.dto.response;

import com.skillswap.backend.entity.SkillPointTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillPointTransactionResponse {

    private String id;
    private SkillPointTransaction.TransactionType type;
    private int amount;
    private int balanceAfter;
    private SkillPointTransaction.Reason reason;
    private String referenceId;
    private String description;
    private Instant createdAt;
}