package com.skillswap.backend.dto.response;

import com.skillswap.backend.entity.Notification.NotificationType;
import lombok.*;

import java.time.Instant;

@Data
@Builder
public class NotificationResponse {
    private String id;
    private NotificationType type;
    private String title;
    private String message;
    private String referenceId;
    private String referenceType;
    private boolean read;
    private Instant createdAt;
}