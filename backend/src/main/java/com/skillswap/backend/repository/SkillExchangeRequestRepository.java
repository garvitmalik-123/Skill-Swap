package com.skillswap.backend.repository;

import com.skillswap.backend.entity.SkillExchangeRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SkillExchangeRequestRepository extends MongoRepository<SkillExchangeRequest, String> {

    List<SkillExchangeRequest> findBySenderIdOrReceiverId(String senderId, String receiverId);

    List<SkillExchangeRequest> findByReceiverIdAndStatus(String receiverId, SkillExchangeRequest.ExchangeStatus status);

    List<SkillExchangeRequest> findBySenderIdAndStatus(String senderId, SkillExchangeRequest.ExchangeStatus status);

    boolean existsBySenderIdAndReceiverIdAndStatus(String senderId, String receiverId, SkillExchangeRequest.ExchangeStatus status);
}