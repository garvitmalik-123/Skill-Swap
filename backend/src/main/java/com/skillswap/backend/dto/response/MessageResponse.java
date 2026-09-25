package com.skillswap.backend.dto.response;

import lombok.*;

import java.time.Instant;

@Data
@Builder
public class MessageResponse {
    private String id;
    private String conversationId;
    private String senderId;
    private String senderName;
    private String content;
    private boolean read;
    private Instant createdAt;
}