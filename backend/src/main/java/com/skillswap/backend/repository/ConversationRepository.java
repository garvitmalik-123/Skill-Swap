package com.skillswap.backend.repository;

import com.skillswap.backend.entity.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends MongoRepository<Conversation, String> {

    @Query("{ 'participantIds': { $all: [?0, ?1] } }")
    Optional<Conversation> findByParticipants(String userIdA, String userIdB);

    @Query("{ 'participantIds': ?0 }")
    List<Conversation> findAllByParticipant(String userId);
}