package com.skillswap.backend.repository;

import com.skillswap.backend.entity.SkillPointWallet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SkillPointWalletRepository extends MongoRepository<SkillPointWallet, String> {

    Optional<SkillPointWallet> findByUserId(String userId);
}