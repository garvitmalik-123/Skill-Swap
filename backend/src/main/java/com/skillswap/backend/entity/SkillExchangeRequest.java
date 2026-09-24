package com.skillswap.backend.entity;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "skill_exchange_requests")
public class SkillExchangeRequest {

    @Id
    private String id;

    @Indexed
    private String senderId;

    @Indexed
    private String receiverId;

    private String senderSkill;     // skill the sender offers to teach
    private String receiverSkill;   // skill the sender wants to learn (receiver teaches)

    private String message;

    @Builder.Default
    private ExchangeStatus status = ExchangeStatus.PENDING;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private Instant completedAt;

    public enum ExchangeStatus {
        PENDING, ACCEPTED, REJECTED, CANCELLED, COMPLETED
    }
}