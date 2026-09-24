package com.skillswap.backend.service;

import com.skillswap.backend.dto.response.SkillPointBalanceResponse;
import com.skillswap.backend.dto.response.SkillPointTransactionResponse;
import com.skillswap.backend.entity.SkillPointTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SkillPointService {

    SkillPointBalanceResponse getBalance(String userId);

    Page<SkillPointTransactionResponse> getTransactionHistory(String userId, Pageable pageable);

    /** Credits points to a user's wallet. Safe to retry with the same idempotencyKey. */
    SkillPointTransactionResponse credit(String userId, int amount, SkillPointTransaction.Reason reason,
                                         String referenceId, String description, String idempotencyKey);

    /** Debits points from a user's wallet. Throws if balance is insufficient. */
    SkillPointTransactionResponse debit(String userId, int amount, SkillPointTransaction.Reason reason,
                                        String referenceId, String description, String idempotencyKey);
}