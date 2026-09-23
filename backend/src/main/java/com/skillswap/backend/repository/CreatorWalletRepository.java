package com.skillswap.backend.repository;

import com.skillswap.backend.entity.CreatorWallet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CreatorWalletRepository extends MongoRepository<CreatorWallet, String> {

    Optional<CreatorWallet> findByCreatorId(String creatorId);
}