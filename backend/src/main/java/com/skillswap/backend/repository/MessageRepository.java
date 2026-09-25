package com.skillswap.backend.repository;

import com.skillswap.backend.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findByConversationIdOrderByCreatedAtAsc(String conversationId);

    long countByConversationIdAndSenderIdNotAndReadFalse(String conversationId, String excludeSenderId);
}