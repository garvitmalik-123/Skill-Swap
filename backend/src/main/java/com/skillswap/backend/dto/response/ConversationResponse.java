package com.skillswap.backend.dto.response;

import lombok.*;

import java.time.Instant;

@Data
@Builder
public class ConversationResponse {
    private String id;
    private String otherUserId;
    private String otherUserName;
    private String lastMessagePreview;
    private Instant lastMessageAt;
    private long unreadCount;
}
