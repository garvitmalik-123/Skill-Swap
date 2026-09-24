package com.skillswap.backend.dto.response;

import com.skillswap.backend.entity.SkillExchangeRequest;
import lombok.*;
import java.time.Instant;

@Data
@Builder
public class ExchangeRequestResponse {
    private String id;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String receiverName;
    private String senderSkill;
    private String receiverSkill;
    private String message;
    private SkillExchangeRequest.ExchangeStatus status;
    private Instant createdAt;
    private Instant completedAt;
}