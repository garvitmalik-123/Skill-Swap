package com.skillswap.backend.repository;

import com.skillswap.backend.entity.SkillPointTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SkillPointTransactionRepository extends MongoRepository<SkillPointTransaction, String> {

    Page<SkillPointTransaction> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    Optional<SkillPointTransaction> findByIdempotencyKey(String idempotencyKey);

    boolean existsByIdempotencyKey(String idempotencyKey);
}