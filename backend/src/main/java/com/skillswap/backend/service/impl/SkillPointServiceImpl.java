package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.response.SkillPointBalanceResponse;
import com.skillswap.backend.dto.response.SkillPointTransactionResponse;
import com.skillswap.backend.entity.SkillPointTransaction;
import com.skillswap.backend.entity.SkillPointWallet;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.repository.SkillPointTransactionRepository;
import com.skillswap.backend.repository.SkillPointWalletRepository;
import com.skillswap.backend.service.SkillPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SkillPointServiceImpl implements SkillPointService {

    private final SkillPointWalletRepository walletRepository;
    private final SkillPointTransactionRepository transactionRepository;

    @Override
    public SkillPointBalanceResponse getBalance(String userId) {
        SkillPointWallet wallet = getOrCreateWallet(userId);
        return SkillPointBalanceResponse.builder()
                .userId(userId)
                .balance(wallet.getBalance())
                .build();
    }

    @Override
    public Page<SkillPointTransactionResponse> getTransactionHistory(String userId, Pageable pageable) {
        return transactionRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::toResponse);
    }

    @Override
    @Retryable(retryFor = OptimisticLockingFailureException.class, maxAttempts = 5, backoff = @Backoff(delay = 50))
    public SkillPointTransactionResponse credit(String userId, int amount, SkillPointTransaction.Reason reason,
                                                String referenceId, String description, String idempotencyKey) {
        if (amount <= 0) {
            throw new BadRequestException("Credit amount must be positive");
        }

        SkillPointTransaction existing = checkIdempotency(idempotencyKey);
        if (existing != null) {
            return toResponse(existing);
        }

        SkillPointWallet wallet = getOrCreateWallet(userId);
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);

        SkillPointTransaction transaction = SkillPointTransaction.builder()
                .userId(userId)
                .type(SkillPointTransaction.TransactionType.CREDIT)
                .amount(amount)
                .balanceAfter(wallet.getBalance())
                .reason(reason)
                .referenceId(referenceId)
                .description(description)
                .idempotencyKey(idempotencyKey)
                .build();

        return toResponse(transactionRepository.save(transaction));
    }

    @Override
    @Retryable(retryFor = OptimisticLockingFailureException.class, maxAttempts = 5, backoff = @Backoff(delay = 50))
    public SkillPointTransactionResponse debit(String userId, int amount, SkillPointTransaction.Reason reason,
                                               String referenceId, String description, String idempotencyKey) {
        if (amount <= 0) {
            throw new BadRequestException("Debit amount must be positive");
        }

        SkillPointTransaction existing = checkIdempotency(idempotencyKey);
        if (existing != null) {
            return toResponse(existing);
        }

        SkillPointWallet wallet = getOrCreateWallet(userId);

        if (wallet.getBalance() < amount) {
            throw new BadRequestException("Insufficient SkillPoint balance");
        }

        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);

        SkillPointTransaction transaction = SkillPointTransaction.builder()
                .userId(userId)
                .type(SkillPointTransaction.TransactionType.DEBIT)
                .amount(amount)
                .balanceAfter(wallet.getBalance())
                .reason(reason)
                .referenceId(referenceId)
                .description(description)
                .idempotencyKey(idempotencyKey)
                .build();

        return toResponse(transactionRepository.save(transaction));
    }

    private SkillPointWallet getOrCreateWallet(String userId) {
        return walletRepository.findByUserId(userId)
                .orElseGet(() -> walletRepository.save(
                        SkillPointWallet.builder().userId(userId).balance(0).build()));
    }

    private SkillPointTransaction checkIdempotency(String idempotencyKey) {
        if (idempotencyKey == null) {
            return null;
        }
        return transactionRepository.findByIdempotencyKey(idempotencyKey).orElse(null);
    }

    private SkillPointTransactionResponse toResponse(SkillPointTransaction transaction) {
        return SkillPointTransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .balanceAfter(transaction.getBalanceAfter())
                .reason(transaction.getReason())
                .referenceId(transaction.getReferenceId())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}