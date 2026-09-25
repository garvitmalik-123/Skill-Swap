package com.skillswap.backend.service.impl;

import com.skillswap.backend.entity.SkillPointTransaction;
import com.skillswap.backend.entity.SkillPointWallet;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.repository.SkillPointTransactionRepository;
import com.skillswap.backend.repository.SkillPointWalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillPointServiceImplTest {

    @Mock
    private SkillPointWalletRepository walletRepository;

    @Mock
    private SkillPointTransactionRepository transactionRepository;

    @InjectMocks
    private SkillPointServiceImpl skillPointService;

    @Test
    void debit_shouldThrowBadRequest_whenBalanceInsufficient() {
        SkillPointWallet wallet = SkillPointWallet.builder()
                .userId("user-1")
                .balance(10)
                .build();

        when(walletRepository.findByUserId("user-1")).thenReturn(Optional.of(wallet));

        assertThatThrownBy(() -> skillPointService.debit(
                "user-1", 50, SkillPointTransaction.Reason.COURSE_ENROLLMENT,
                "course-1", "Enrollment", "idem-key-1"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Insufficient");
    }

    @Test
    void credit_shouldReturnExistingTransaction_whenIdempotencyKeyAlreadyUsed() {
        SkillPointTransaction existing = SkillPointTransaction.builder()
                .id("txn-1")
                .userId("user-1")
                .type(SkillPointTransaction.TransactionType.CREDIT)
                .amount(20)
                .balanceAfter(20)
                .idempotencyKey("idem-key-1")
                .build();

        when(transactionRepository.findByIdempotencyKey("idem-key-1")).thenReturn(Optional.of(existing));

        var response = skillPointService.credit(
                "user-1", 20, SkillPointTransaction.Reason.TEACHING_REWARD,
                "ref-1", "Reward", "idem-key-1");

        assertThat(response.getId()).isEqualTo("txn-1");
        assertThat(response.getAmount()).isEqualTo(20);
    }
}