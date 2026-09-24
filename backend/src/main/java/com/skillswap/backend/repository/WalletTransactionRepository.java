package com.skillswap.backend.repository;

import com.skillswap.backend.entity.WalletTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WalletTransactionRepository extends MongoRepository<WalletTransaction, String> {

    Page<WalletTransaction> findByCreatorIdOrderByCreatedAtDesc(String creatorId, Pageable pageable);
}